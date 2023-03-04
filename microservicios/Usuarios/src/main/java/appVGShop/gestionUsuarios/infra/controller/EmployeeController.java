package appVGShop.gestionUsuarios.infra.controller;

import appVGShop.gestionUsuarios.application.service.EmployeeService;
import appVGShop.gestionUsuarios.domain.dto.EmployeeDTOCreator;
import appVGShop.gestionUsuarios.domain.dto.LoginDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.net.URL;
import java.security.*;
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

                //Generamos un nuevo registro en el fichero accesos.log cada vez que se crea un usuario
                generateAccessLog(newUserCreator.getNombreEmpleado(), "New User", "POST");

                //Generamos la clave secreta para poder encriptar y desencriptar los ficheros
                //generateSecretKey();

                //Encriptamos el fichero accesos.log con la clave secreta y generamos un fichero nuevo (accesos_cifradosimetrico.log)
                encryptSymmetricFile();

                //Desencriptamos el fichero accesos_cifradosimetrico.log y generamos un fichero nuevo (accesos_descifradosimetrico.log)
                descryptSymmetricFile();

                //Firmamos el fichero accesos.log con la clave privada y creamos un fichero nuevo (accesos.firma) y comprobamos la firma con la clave pública
                generateSignFile();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return employeeService.newUser(newUserCreator);
    }

    @Override
    public ResponseEntity<?> editUser(EmployeeDTOCreator editData, Integer id) {
        //Generamos un nuveo registro en el fichero accesos.log cada vez que se actualiza un usuario
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
            //Comprobamos si el fichero accesos.log existe y si no existe lo creamos
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
                //El fichero accesos.log tiene la siguiente estructura: NOMBRE - FECHA - HORA - OPERACIÓN - MÉTODO
                file.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void encryptSymmetricFile() {

        try {
            //Tomamos la clave SECRETA del fichero
            ObjectInputStream oin = new ObjectInputStream(new FileInputStream("clave.secreta"));
            Key clavesecreta = (Key) oin.readObject();
            oin.close();

            //Se define instancia de Cipher para encriptar con la clave secreta
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, clavesecreta);

            //Se toma el archivo de log
            FileInputStream filein = new FileInputStream("accesos.log");

            //...y posteriormente se encripta utilizando el objeto Cipher definido antes
            CipherOutputStream out = new CipherOutputStream(new FileOutputStream("accesos_cifradosimetrico.log"), c);
            int tambloque = c.getBlockSize(); // tamaño de bloque del objeto Cipher
            byte[] bytes = new byte[tambloque]; // Bloque de bytes

            //Leemos los bloques de bytes del fichero y los escribimos usando CipherOutputStream
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

    public static void descryptSymmetricFile() {

        try {
            //Tomamos la clave SECRETA del fichero
            ObjectInputStream oin = new ObjectInputStream(new FileInputStream("clave.secreta"));
            Key clavesecreta = (Key) oin.readObject();
            oin.close();

            //Se define instancia de Cipher para desencriptar con la clave secreta
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, clavesecreta);

            //Se toma el archivo de log cifrado con clave secreta
            CipherInputStream in = new CipherInputStream(new FileInputStream("accesos_cifradosimetrico.log"), c);

            int tambloque = c.getBlockSize();   //tamaño de bloque
            byte[] bytes = new byte[tambloque]; //bloque en bytes

            //Definimos el archivo de log donde acabará el texto descifrado
            FileOutputStream fileout = new FileOutputStream("accesos_descifradosimetrico.log");

            //Se leen los bloques de bytes del fichero cifrado, escribiéndolos una vez desencriptados
            int i = in.read(bytes);
            while (i != -1) {
                fileout.write(bytes, 0, i);
                i = in.read(bytes);
            }
            fileout.close();
            in.close();
            System.out.println("Fichero descifrado con clave secreta.");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void generateSignFile() {
        try {
            //Tomamos la clave PRIVADA del fichero
            FileInputStream inpriv = new FileInputStream("clave.privada");
            byte[] bufferPriv = new byte[inpriv.available()];
            inpriv.read(bufferPriv);
            inpriv.close();

            //Se importa el contenido del archivo a la clase PKCS8EncodedKeySpec y luego a PrivateKey
            PKCS8EncodedKeySpec clavePrivadaSpec = new PKCS8EncodedKeySpec(bufferPriv);
            KeyFactory keyDSA = KeyFactory.getInstance("DSA");
            PrivateKey clavePrivada = keyDSA.generatePrivate(clavePrivadaSpec);

            //Se inicia la firma con la clave privada
            Signature dsa = Signature.getInstance("SHA256withDSA");
            dsa.initSign(clavePrivada);

            //Se lee el archivo a firmar y se le proporciona la Signature generada
            FileInputStream ficheroAFirmar = new FileInputStream("accesos.log");
            BufferedInputStream bis = new BufferedInputStream(ficheroAFirmar);
            byte[] buffer = new byte[bis.available()];
            int len;

            while ((len = bis.read(buffer)) >= 0) {
                dsa.update(buffer, 0, len);
            }

            bis.close();

            //Genera la firma como un array de bytes
            byte[] firma = dsa.sign();

            //Y se guarda en otro fichero
            FileOutputStream fos = new FileOutputStream("accesos.firma");
            fos.write(firma);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //Toma la clave PÚBLICA del fichero
            FileInputStream inpub = new FileInputStream("clave.publica");
            byte[] bufferPub = new byte[inpub.available()];
            inpub.read(bufferPub);  //lectura de bytes
            inpub.close();

            //Se importa el contenido del archivo a la clase X509EncodedKeySpec y luego a PublicKey
            KeyFactory keyDSA = KeyFactory.getInstance("DSA");
            X509EncodedKeySpec clavePublicaSpec = new X509EncodedKeySpec(bufferPub);
            PublicKey clavePublica = keyDSA.generatePublic(clavePublicaSpec);

            //Se lee el archivo que contiene la firma
            FileInputStream firmafic = new FileInputStream("accesos.firma");
            byte[] firma = new byte[firmafic.available()];
            firmafic.read(firma);
            firmafic.close();

            //Se inicia el objeto Signature para verificar utilizando la clave pública
            Signature dsa = Signature.getInstance("SHA256withDSA");
            dsa.initVerify(clavePublica);

            //Lee el fichero con los datos a verificar
            FileInputStream ficheroOriginal = new FileInputStream("accesos.log");
            BufferedInputStream bis = new BufferedInputStream(ficheroOriginal);
            byte[] buffer = new byte[bis.available()];
            int len;

            while ((len = bis.read(buffer)) >= 0) {
                dsa.update(buffer, 0, len);
            }
            bis.close();

            //Y se verifica la firma para ver si los datos corresponden
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
