package com.allbib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public class SocketClientCallable implements Callable<String> {
	private final String host;
	private final int port;
	private final String command;
	private final String data;

	public SocketClientCallable(String host, int port, String command, String data) {
		this.host = host;
		this.port = port;
		this.command = command;
		this.data = data;
	}

	@Override
	public String call() {
		// Use a try-with resources to instantiate the client socket and
		// buffers for reading and writing messages from and to the server
		try (Socket socket = new Socket(this.host, this.port);
			 BufferedWriter bufferedOutputWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			 BufferedReader bufferedInputReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)))
		{
			// Send command to the server
			bufferedOutputWriter.write(command + "\n" + data);
			bufferedOutputWriter.newLine();
			bufferedOutputWriter.flush();

			// Get the message from the server
			return bufferedInputReader.readLine();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
