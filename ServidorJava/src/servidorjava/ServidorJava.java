import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorJava
{
  int puerto = 8000;
  
  void MensajeServidor(String mensaje)
  {
    System.out.println("Mensaje: " + mensaje);
  }
  
  public static void main(String[] array)
  {
    ServidorJava instancia = new ServidorJava();
    instancia.IniciarServidor();
  }
  
  public boolean IniciarServidor()
  {
    MensajeServidor("Servidor Iniciado");
    try
    {
      ServerSocket s = new ServerSocket(8000);
      MensajeServidor("Esperando Conexion......");
      while(true)
      {
        Socket cliente = s.accept();
        Peticion pCliente = new Peticion(cliente);
        pCliente.start();
      }
    }
    catch (Exception e)
    {
      MensajeServidor("Error en servidor\n" + e.toString());
    }
    return true;
  }
}
