import java.io.{DataInputStream, DataOutputStream}
import java.net.Socket
import scala.io.StdIn.readLine

class Client {
  var client: Option[Socket] = None
  var clientInput: String = ""

  def runClient(): Unit = {
    // println("Enter 'quit' to exit the app")
    while(clientInput != "quit") {
      try {
        client = Some(new Socket("127.0.0.1", 8000))  //IP address points to local host, seaching for socket 8000
        println("\nEnter 'quit' to exit the app.\n")
        val is = new DataInputStream(client.get.getInputStream)
        val os = new DataOutputStream(client.get.getOutputStream)
        
        //obtaining user inputs
        clientInput = readLine("Enter an ISBN value: ")
        
        // exit is the sentinel value, if entered the client will be disconnected
        if (clientInput != "exit"){
          // sends the user inputs
          os.writeBytes(clientInput + "\n")
          var line: String = is.readLine()
          println(line)
        }
      } catch {
        case e: Exception => e.printStackTrace()
      } finally {
        client foreach (_.close()) //for closing the client
      }
    }
  }
}