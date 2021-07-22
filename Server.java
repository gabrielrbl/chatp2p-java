import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Server {
	private static final int PORTA = 1234;
	private static DatagramSocket datagramSocket;
	private static DatagramPacket inPacket, outPacket;
	private static byte[] buffer;

	public static void main(String[] args) {
		System.out.println("Abrindo conexão...");

		try {
			datagramSocket = new DatagramSocket(PORTA); // passo 1
		} catch (SocketException e) {
			System.out.println("Não foi possível estabelecer conexão!");
			System.exit(1);
		}
		client();
	}

	private static void client() {
		try {
			Scanner userEntry = new Scanner(System.in);

			String mensagem = "", resposta = "";

			do {
				buffer = new byte[65507]; // passo 2

				inPacket = new DatagramPacket(buffer, buffer.length); // passo 3

				datagramSocket.receive(inPacket); // passo 4

				InetAddress enderecoCliente = inPacket.getAddress(); // passo 5
				int portaCliente = inPacket.getPort(); // passo 5

				resposta = new String(inPacket.getData(), 0, inPacket.getLength()); // passo 6
				System.out.println("CLIENTE: " + resposta);
				
				System.out.print("Digite a mensagem: ");
				mensagem = userEntry.nextLine();

				if (!mensagem.equals("s")) {
					// passo 2
					outPacket = new DatagramPacket(mensagem.getBytes(), mensagem.length(), enderecoCliente, portaCliente); // passo 7

					datagramSocket.send(outPacket); // passo 8
				}
			} while (!mensagem.equals("s"));
			userEntry.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Fechando conexão...");
			datagramSocket.close(); // passo 9
		}
	}
}
