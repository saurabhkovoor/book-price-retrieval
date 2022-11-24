import scala.io.StdIn.readLine

object App{
	def main(args: Array[String]): Unit = {
		var input: String = "0"
		var validInput: Boolean = false
        
        //Menu function, display menu options, collect user input and perform corresponding action
		while(!validInput)
		try {
			input = readLine("1: Start server\n2: Create a client\n3: Exit the Application\nChoose your selection (1-3): ")
			input match {
				case "1" => startServer()
							validInput = true
				case "2" => createClient()
							validInput = true
				case "3" => validInput = true
				case _ => println("Error: Enter a valid selection (1-3).")
			}
		} catch {
			case e: Throwable => println()
		}
	}

    //function to create and start server
	def startServer() {
		println("Starting Server...")
		val server: Server = new Server()
		server.runServer()
	}

    //function to create and start client
	def createClient() {
		println("Creating Client...")
		val client: Client = new Client()
		client.runClient()
	}
}