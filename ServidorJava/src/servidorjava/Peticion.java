import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import static java.lang.Thread.currentThread;
import java.net.Socket;
import java.net.URL;
import java.util.StringTokenizer;

public class Peticion extends Thread
{
  void MensajeServidor(String mensaje)
  {
    System.out.println(currentThread().toString() + " - " + mensaje);
  }
  
  private Socket cliente = null;
  private PrintWriter salida = null;
  
  Peticion(Socket clienteExt)
  {
    cliente = clienteExt;
    setPriority(4);
  }
  
  public void run()
  {
    MensajeServidor("Procesando conexion");
    try
    {
      BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
      salida = new PrintWriter(new OutputStreamWriter(cliente.getOutputStream(), "8859_1"), true);
      

      String cadena = "";
     
      int i = 0;
      do
      {
        cadena = entrada.readLine();
        if (cadena != null) {
          MensajeServidor("--" + cadena);
        }
        if (i == 0)
        {
          i++;
          
          StringTokenizer st = new StringTokenizer(cadena);
          if ((st.countTokens() >= 2) && (st.nextToken().equals("GET"))) {
            RetornarArchivo(st.nextToken());
          } else {
            salida.println("400 Petición Incorrecta");
          }
        }
        if (cadena == null) {
          break;
        }
      } while (cadena.length() != 0);
    }
    catch (Exception e)
    {
      salida.println("HTTP/1.1 400 Not Found");
      salida.close();
    }
    MensajeServidor("Peticion Finalizada");
  }
  
  void RetornarArchivo(String archivo)
  {
    MensajeServidor("Obteniendo el archivo " + archivo);
    if (archivo.startsWith("/")) {
      archivo = archivo.substring(1);
    }
    if ((archivo.endsWith("/")) || (archivo.equals(""))) {
      archivo = archivo + "index.html";
    }
    
    String basePath = new File("").getAbsolutePath();
    System.out.println(basePath);
    try
    {
      System.out.println(archivo);
      File mifichero = new File(archivo);
      if (mifichero.exists())
      {
        if (archivo.endsWith("html"))
        {
          salida.println("HTTP/1.1 200 ok");
          

          salida.println("Content-Type: text/html");
          salida.println("Content-Length: " + mifichero.length());
          salida.println("\n");
        }
        else if (archivo.endsWith("css"))
        {
          salida.println("HTTP/1.1 200 ok");
          

          salida.println("Content-Type: text/css");
          salida.println("Content-Length: " + mifichero.length());
          salida.println("\n");
        }
        else if (archivo.endsWith("js"))
        {
          salida.println("HTTP/1.1 200 ok");
          

          salida.println("Content-Type: application/javascript");
          salida.println("Content-Length: " + mifichero.length());
          salida.println("\n");
        }
        BufferedReader ficheroLocal = new BufferedReader(new FileReader(mifichero));
        

        String linea = "";
        do
        {
          linea = ficheroLocal.readLine();
          if (linea != null) {
            salida.println(linea);
          }
        } while (linea != null);
        MensajeServidor("Archivo enviado");
        
        ficheroLocal.close();
        salida.close();
        cliente.close();
      }
      else
      {
        MensajeServidor("No encuentro el archivo " + mifichero.toString());
        cliente.close();
      }
    }
    catch (Exception e)
    {
        e.printStackTrace();
      MensajeServidor("Error al retornar archivo");
    }
  }
}