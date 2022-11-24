import java.net.{ServerSocket, Socket}
import java.io.{DataInputStream, DataOutputStream}

import scala.io.Source
import scala.collection.mutable.HashMap
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Server {
  // create server at socket val 8000
  val server = new ServerSocket(8000)
  // initialise hashmap holding book repository isbn - price combinations
  var bookRepo: HashMap[String, String] = HashMap[String, String]()
  
  //main function to create server, initialise book repo, and handle client connection request
  def runServer(): Unit = {
    println("Server running with socket 8000:\n\n")
    createBookRepo()

    //handling client
    while(true) {
      val client: Socket = server.accept()
      try{
        Future {
            println("Client has connected")
            try {
                val is = new DataInputStream(client.getInputStream())
                val os = new DataOutputStream(client.getOutputStream())
                var clientInput: String = is.readLine()
                
                if(clientInput!="quit") {
                    println(s"Client is trying to find the price for ISBN ${clientInput}")
                    os.writeBytes(findPrice(clientInput) + "\n")
                }
            } catch {
                case e: Exception => println()
            } finally { //print message when client is disconnecting from server
                client.close()
                println("Client has disconnected")
            }
        }} catch { // error when connection request is unsuccessful
            case e: Exception => println("A connection error has occurred.")
        }
        }
    }

  // function to initialise book repository in a hashmap object
  def createBookRepo(): Unit ={
    println("Creating book repository...")
    val bufferedSource = Source.fromFile("book.csv") //starts a connection with the csv file holding book data
    
    // obtaining all the lines of the csv files
    for (line <- bufferedSource.getLines()) {
      //splits the isbn and price information by comma and storing to aforementioned book repo
      val isbn_price = line.split(",").map(_.trim)
      bookRepo += (isbn_price(0) -> isbn_price(1))
    }
    bufferedSource.close() // close connection with the csv file once values have been retrieved

    println("Book repository created")
    println(s"Server created and launched at Port: 8000")
  }
  
  // function to find the price value when the isbn value is provided
  def findPrice(isbn: String): String = {
    // checking if isbn value exists in the database/hashmap
    if(bookRepo.contains(isbn)){
      return (s"ISBN: '${isbn}'\tPrice: USD${bookRepo(isbn)}")
    } else {
      return (s"ISBN '${isbn}' is not found in the book repository. Try entering a different ISBN code.")
    }
  }
}