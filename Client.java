import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	private static InetAddress host;
	private static final int PORTA = 1234;
	private static DatagramSocket datagramSocket;
	private static DatagramPacket inPacket, outPacket;
	private static byte[] buffer;

	public static void main(String[] args) throws IOException {
		try {
			host = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.out.println("Host não encontrado!");
			System.exit(1);
		}
		acessarServidor();
	}

	private static void acessarServidor() throws IOException {
		try {
			// passo 1
			datagramSocket = new DatagramSocket();

			Scanner userEntry = new Scanner(System.in);

			String mensagem = "", resposta = "";

			do {
				System.out.print("Digite a mensagem: ");
				mensagem = userEntry.nextLine();

				if (!mensagem.equals("s")) {
					// passo 2
					outPacket = new DatagramPacket(mensagem.getBytes(), mensagem.length(), host, PORTA);

					datagramSocket.send(outPacket); // passo 3
					buffer = new byte[65507]; // passo 4

					inPacket = new DatagramPacket(buffer, buffer.length); // passo 5

					datagramSocket.receive(inPacket);
					resposta = new String(inPacket.getData(), 0, inPacket.getLength()); // passo 6
					System.out.println("SERVIDOR: " + resposta);
				}
			} while (!mensagem.equals("s"));
			userEntry.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Fechando conexão...");
			datagramSocket.close();
		}
	}
}
