import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class UDPServerChat {
	private static final int PORTA = 1234;
	private static DatagramSocket datagramSocket;
	private static DatagramPacket inPacket, outPacket;
	private static byte[] buffer;

	public static void main(String[] args) {
		System.out.println("Iniciando chat...");

		try {
			datagramSocket = new DatagramSocket(PORTA);
		} catch (SocketException e) {
			System.out.println("Não foi possível estabelecer conexão!");
			System.exit(1);
		}
		client();
	}

	private static void client() {
		try {
			Scanner scanner = new Scanner(System.in);
			String nickname = "", mensagem = "", resposta = "";
			
			do {
				System.out.print("Digite seu nickname: ");
				nickname = scanner.nextLine();
			} while (nickname.equals(""));

			System.out.println("Bem vindo " + nickname + ", vamos iniciar o chat. ");
			System.out.println("Observação: para finalizar o chat digite -1");

			System.out.println("Aguardando mensagem...");

			do {
				buffer = new byte[65507];
				inPacket = new DatagramPacket(buffer, buffer.length);
				datagramSocket.receive(inPacket);

				InetAddress enderecoCliente = inPacket.getAddress();
				int portaCliente = inPacket.getPort();

				resposta = new String(inPacket.getData(), 0, inPacket.getLength());

				if (!resposta.equals("-1")) {
					System.out.println("Mensagem recebida: " + resposta);
				} else {
					System.out.println("Seu amigo(a) finalizou o chat...");
					System.exit(1);
				}

				System.out.print("Digite sua mensagem: ");
				mensagem = scanner.nextLine();

				if (!mensagem.equals("-1")) {
					outPacket = new DatagramPacket(mensagem.getBytes(), mensagem.length(), enderecoCliente, portaCliente);
					datagramSocket.send(outPacket);

					System.out.println("Aguardando mensagem...");
				}
			} while (!mensagem.equals("-1"));
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Finalizando chat...");
			datagramSocket.close();
		}
	}
}
