package appVGShop.gestionUsuarios.infra.controller;

import appVGShop.gestionUsuarios.application.service.EmployeeService;
import appVGShop.gestionUsuarios.domain.dto.EmployeeDTOCreator;
import appVGShop.gestionUsuarios.domain.dto.LoginDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URL;
import java.security.*;
import javax.crypto.*;
import java.util.Calendar;

@RestController
@AllArgsConstructor
public class EmployeeController implements EmployeeAPI {

    private final EmployeeService employeeService;

    @Override
    public ResponseEntity<?> getAll() {
        return employeeService.getAll();
    }

    @Override
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        return employeeService.getUser(id);
    }

    @Override
    public ResponseEntity<?> newUser(@RequestBody EmployeeDTOCreator newUserCreator) {

        try {
            //Obtener texto de clave
            URL resourcekey = getClass().getClassLoader().getResource("clave.txt");
            if (resourcekey == null) {
                throw new IllegalArgumentException("No se encuentra el archivo.");
            } else {
                File archivoclave = new File(resourcekey.toURI());
                FileReader frclave = new FileReader(archivoclave);
                BufferedReader brclave = new BufferedReader(frclave);
                String clave = brclave.readLine();

                MessageDigest md = MessageDigest.getInstance("SHA-256");

                byte[] bytespass = newUserCreator.getPasswdEmpleado().getBytes();
                md.update(bytespass);
                byte[] resumenpass = md.digest(clave.getBytes());

                newUserCreator.setPasswdEmpleado(new String(resumenpass));

                generateAccessLog(newUserCreator.getNombreEmpleado(), "New User", "POST");
                generateSecretKey();
                encryptSymmetricFile();
                decryptSymmetricFile();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return employeeService.newUser(newUserCreator);
    }

    @Override
    public ResponseEntity<?> editUser(EmployeeDTOCreator editData, Integer id) {
        generateAccessLog(editData.getNombreEmpleado(), "Edit User", "PUT");
        return employeeService.editUser(editData, id);

    }

    @Override
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        return employeeService.deleteUser(id);

    }

    @Override
    public ResponseEntity<?> login(LoginDTO login) {
        return employeeService.login(login);
    }

    public void generateAccessLog(String user, String resource, String operation) {

        try {
            if (!new File("resources/accesos.log").exists()) {
                FileWriter file = new FileWriter("accesos.log", true);
                Calendar actualDate = Calendar.getInstance();
                file.write("" + user
                        + " - " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + "/" + (actualDate.get(Calendar.MONTH) + 1)
                        + "/" + actualDate.get(Calendar.YEAR)
                        + " - " + actualDate.get(Calendar.HOUR_OF_DAY)
                        + ":" + actualDate.get(Calendar.MINUTE)
                        + ":" + actualDate.get(Calendar.SECOND)
                        + " - " + resource
                        + " - " + operation
                        + "\r\n");
                file.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

       public static void generateSecretKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);

            // Generate secret key
            SecretKey key = kg.generateKey();

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("clave.secreta"));
            out.writeObject(key);
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void encryptSymmetricFile() {
        try {

            ObjectInputStream in = new ObjectInputStream(new FileInputStream("clave.secreta"));
            Key secretKey = (Key)in.readObject();


            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, secretKey);

            in = new ObjectInputStream(new FileInputStream("accesos.log"));
            byte[] logFile = (byte[]) in.readObject();
            in.close();

            byte[] encryptedFile = c.doFinal(logFile);
            System.out.println("Encrypted: " + new String(encryptedFile));

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("accesos_cifradosimetrico.log"));
            out.writeObject(encryptedFile);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void decryptSymmetricFile() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("clave.secreta"));
            Key secretKey = (Key) in.readObject();

            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, secretKey);

            in = new ObjectInputStream(new FileInputStream("accesos_cifradosimetrico.log"));
            byte[] logFile = (byte[]) in.readObject();
            in.close();

            byte[] decryptedFile = c.doFinal(logFile);
            System.out.println("Decrypted: " + new String(decryptedFile));

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("accesos_descifradosimetrico.log"));
            out.writeObject(decryptedFile);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    public static void generatePairKeysAndSignFile() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);

            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();

            // Read the target file
            byte[] logFile = (byte[]) readFile("access.log");

            // Sign the file with the private key
            // Give the data to the Signature object
            Signature dsa = Signature.getInstance("SHA256withDSA");
            dsa.initSign(privateKey);
            dsa.update(logFile);

            // The signed file contents
            byte[] sign = dsa.sign();

            // Write the signed file in a binary file
//            FileOutputStream outSign = new FileOutputStream("access.signature");
//            outSign.write(sign);
//            outSign.close();
            writeFile("access.signature", sign);

            // The message receiver verifies with the public key the signed contents
            // The Signature object is provided the data to verify
            Signature checkDSA = Signature.getInstance("SHA256withDSA");
            checkDSA.initVerify(publicKey);
            checkDSA.update(logFile);
            boolean check = checkDSA.verify(sign);

            if (check) {
                //OK
                System.out.println("Verified signature with the public key");
            } else {
                System.out.println("ERROR: Signature not verified");
            }

            PKCS8EncodedKeySpec pkcs8Spec = new PKCS8EncodedKeySpec(privateKey.getEncoded());

            // Write the private key in a binary file
//            FileOutputStream outpriv = new FileOutputStream("key.private");
//            outpriv.write(pkcs8Spec.getEncoded());
//            outpriv.close();
            writeFile("key.private", pkcs8Spec.getEncoded());

            X509EncodedKeySpec pkX509 = new X509EncodedKeySpec(publicKey.getEncoded());

            // Write public key in a binary file
//            FileOutputStream outpub = new FileOutputStream("key.public");
//            outpub.write(pkX509.getEncoded());
//            outpub.close();
            writeFile("key.public", pkX509.getEncoded());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
}
