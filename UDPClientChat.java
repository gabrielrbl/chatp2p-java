import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UDPClientChat {
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
		System.out.println("Iniciando chat...");

		try {
			datagramSocket = new DatagramSocket();

			Scanner scanner = new Scanner(System.in);
			String nickname = "", mensagem = "", resposta = "";

			System.out.print("Digite seu nickname: ");
			nickname = scanner.nextLine();

			System.out.println("Bem vindo " + nickname + ", vamos iniciar o chat. ");
			System.out.println("Observação: para finalizar o chat digite -1");

			do {
				System.out.print("Digite sua mensagem: ");
				mensagem = scanner.nextLine();

				if (!mensagem.equals("-1")) {
					outPacket = new DatagramPacket(mensagem.getBytes("UTF-8"), mensagem.length(), host, PORTA);
					datagramSocket.send(outPacket);

					System.out.println("Aguardando mensagem...");

					buffer = new byte[65507];
					inPacket = new DatagramPacket(buffer, buffer.length);
					datagramSocket.receive(inPacket);

					resposta = new String(inPacket.getData(), 0, inPacket.getLength(), "UTF-8");
					System.out.println("Mensagem recebida: " + resposta);
				}
			} while (!mensagem.equals("-1"));
			scanner.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Finalizando chat...");

			outPacket = new DatagramPacket("-1".getBytes("UTF-8"), "-1".length(), host, PORTA);
			datagramSocket.send(outPacket);

			datagramSocket.close();
		}
	}
}
