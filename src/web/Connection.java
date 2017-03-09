package web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
// **************** Start code by Edward Ekstrom ****************
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
// **************** End code by Edward Ekstrom ****************
import java.net.Socket;

import web.HttpRequest.HttpMethod;
import web.HttpResponse.StatusCode;

public class Connection implements Runnable {

	private Server server;
	private Socket client;
	private InputStream in;
	private OutputStream out;

	public Connection(Socket cl, Server s) {
		client = cl;
		server = s;
	}

	@Override
	public void run() {
		try {
			in = client.getInputStream();
			out = client.getOutputStream();

			HttpRequest request = HttpRequest.parseAsHttp(in);
			
			if (request != null) {
				System.out.println("Request for " + request.getUrl() + " is being processed " +
					"by socket at " + client.getInetAddress() +":"+ client.getPort());
				
				HttpResponse response;
				
				String method;
				if ((method = request.getMethod()).equals(HttpMethod.GET) || method.equals(HttpMethod.HEAD)) {
					File f = new File(server.getWebRoot() + request.getUrl());
					response = new HttpResponse(StatusCode.OK).withFile(f);
					if (method.equals(HttpMethod.HEAD)) {
						response.removeBody();
					}
// **************** Start code by Edward Ekstrom ****************	
				} else if (method.equals(HttpMethod.PUT)) {
					response = new HttpResponse(StatusCode.OK);

					File outFile = new File(server.getWebRoot() + request.getUrl());
					outFile.getParentFile().mkdirs();
					outFile.createNewFile();

					Writer writer = null;
					try {
					    writer = new BufferedWriter(new FileWriter(outFile));
					    writer.write(request.getBodyAsString());
					    response.withHtmlBody(request.getUrl());
					} catch (IOException ex) {
					  System.err.println("Errer while writing to file.");
					} finally {
					   try {writer.close();} catch (Exception ex) {/*ignore*/}
					}
// **************** End code by Edward Ekstrom ****************
				} else {
					response = new HttpResponse(StatusCode.NOT_IMPLEMENTED);
				}
				
				respond(response);
				
			} else {
				System.err.println("Server accepts only HTTP protocol.");
			}
			
			in.close();
			out.close();
		} catch (IOException e) {
			System.err.println("Error in client's IO.");
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				System.err.println("Error while closing client socket.");
			}
		}
	}

	public void respond(HttpResponse response) {
		String toSend = response.toString();
		PrintWriter writer = new PrintWriter(out);
		writer.write(toSend);
		writer.flush();
	}

}
