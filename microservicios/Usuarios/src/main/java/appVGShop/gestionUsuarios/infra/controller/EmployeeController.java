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
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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
                //generateSecretKey();
                encryptSymmetricFile();
                decryptSymmetricFile();
                generateSignFile();
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

    public static void encryptSymmetricFile() {

        try {
            // RECUPERAMOS CLAVE SECRETA DEL FICHERO
            ObjectInputStream oin = new ObjectInputStream(new FileInputStream("Clave.secreta"));
            Key clavesecreta = (Key) oin.readObject();
            oin.close();

            // SE DEFINE EL OBJETO Cipher para encriptar
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, clavesecreta);

            // FICHERO A CIFRAR
            FileInputStream filein = new FileInputStream("accesos.log");

            // OBJETO cipherOutputStream QUE ENCRIPTA EL FICHERO
            CipherOutputStream out = new CipherOutputStream(new FileOutputStream("accesos_cifradosimetrico.log"), c);
            int tambloque = c.getBlockSize(); // tamaÃ±o de bloque objeto Cipher
            byte[] bytes = new byte[tambloque]; // bloque de bytes

            // LEEMOS BLOQUES DE BYTES DEL FICHERO PDF
            // Y LO VAMOS ESCRIBIENDO AL CipherOutputStream
            int i = filein.read(bytes);
            while (i != -1) {
                out.write(bytes, 0, i);
                i = filein.read(bytes);
            }
            out.flush();
            out.close();
            filein.close();
            System.out.println("Fichero cifrado con clave secreta.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void decryptSymmetricFile() {

        try {
            //RECUPERAMOS CLAVE SECRETA DEL FICHERO
            ObjectInputStream oin = new ObjectInputStream(new FileInputStream("clave.secreta"));
            Key clavesecreta = (Key) oin.readObject();
            oin.close();

            //SE DEFINE EL OBJETO Cipher PARA DESENCRIPTAR
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, clavesecreta);

            //OBJETO CipherInputStream CUYO CONTENIDO SE VA A DESCIFRAR
            CipherInputStream in = new CipherInputStream(new FileInputStream("accesos_cifradosimetrico.log"), c);

            int tambloque = c.getBlockSize();   //tamaño de bloque
            byte[] bytes = new byte[tambloque]; //bloque en bytes

            //FICHERO CON EL CONTENIDO DESCIFRADO QUE SE CREARÁ
            FileOutputStream fileout = new FileOutputStream("accesos_descifradosimetrico.log");

            //LEEMOS BLOQUES DE BYTES DEL FICHERO cifrado
            //Y LO VAMOS ESCRIBIENDO DESENCRIPTADOS AL FileOutputStream
            int i = in.read(bytes);
            while (i != -1) {
                fileout.write(bytes, 0, i);
                i = in.read(bytes);
            }
            fileout.close();
            in.close();
            System.out.println("Fichero descifrado con clave secreta.");

        } catch (Exception e ) {
            e.printStackTrace();
        }

    }

    public static void generateSignFile() {
        try {
            //LECTURA DEL FICHERO DE CLAVE PRIVADA
            FileInputStream inpriv = new FileInputStream("clave.privada");
            byte[] bufferPriv = new byte[inpriv.available()];
            inpriv.read(bufferPriv);
            inpriv.close();

            //RECUPERA CLAVE PRIVADA DESDE DATOS CODIFICADOS EN FORMATO PKCS8
            PKCS8EncodedKeySpec clavePrivadaSpec = new PKCS8EncodedKeySpec(bufferPriv);
            KeyFactory keyDSA = KeyFactory.getInstance("DSA");
            PrivateKey clavePrivada = keyDSA.generatePrivate(clavePrivadaSpec);

            //INICIANDO FIRMA CON CLAVE PRIVADA
            Signature dsa = Signature.getInstance("SHA256withDSA");
            dsa.initSign(clavePrivada);

            //LECTURA DEL FICHERO A FIRMAR
            //Se suministra al objeto Signature los datos a firmar
            FileInputStream ficheroAFirmar = new FileInputStream("accesos.log");
            BufferedInputStream bis = new BufferedInputStream(ficheroAFirmar);
            byte[] buffer = new byte[bis.available()];
            int len;

            while ((len = bis.read(buffer)) >= 0) {
                dsa.update(buffer, 0, len);
            }

            bis.close();

            //GENERA LA FIRMA EN OTRO DE LOS DATOS DEL FICHERO
            byte[] firma = dsa.sign();

            //GUARDA LA FIRMA EN OTRO FICHERO
            FileOutputStream fos = new FileOutputStream("accesos.firma");
            fos.write(firma);
            fos.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
        try {
            //LECTURA DE LA CLAVE PÚBLICA DEL FICHERO
            FileInputStream inpub = new FileInputStream("clave.publica");
            byte[] bufferPub = new byte[inpub.available()];
            inpub.read(bufferPub);  //lectura de bytes
            inpub.close();

            //RECUPERA CLAVE PUBLICA DESDE DATOS CODIFICADOS EN FORMATO X509
            KeyFactory keyDSA = KeyFactory.getInstance("DSA");
            X509EncodedKeySpec clavePublicaSpec = new X509EncodedKeySpec(bufferPub);
            PublicKey clavePublica = keyDSA.generatePublic(clavePublicaSpec);

            //LECTURA DEL FICHERO QUE CONTIENE LA FIRMA
            FileInputStream firmafic = new FileInputStream("accesos.firma");
            byte[] firma = new byte[firmafic.available()];
            firmafic.read(firma);
            firmafic.close();

            //INICIALIZA EL OBJETO Signature CON CLAVE PÚBLICA PARA VERIFICAR
            Signature dsa = Signature.getInstance("SHA256withDSA");
            dsa.initVerify(clavePublica);

            //LECTURA DEL FICHERO QUE CONTIENE LOS DATOS A VERIFICAR
            //Se suministra al objeto Signature los datos a verificar
            FileInputStream ficheroOriginal = new FileInputStream("accesos.log");
            BufferedInputStream bis = new BufferedInputStream(ficheroOriginal);
            byte[] buffer = new byte[bis.available()];
            int len;

            while ((len = bis.read(buffer)) >= 0) {
                dsa.update(buffer, 0, len);
            }
            bis.close();

            //VERIFICA LA FIRMA DE LOS DATOS LEIDOS
            boolean verifica = dsa.verify(firma);

            //COMPROBAR LA VERIFICACIÓN
            if (verifica) {
                System.out.println("LOS DATOS SE CORRESPONDEN CON SU FIRMA.");
            } else {
                System.out.println("LOS DATOS NO SE CORRESPONDEN CON SU FIRMA");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
