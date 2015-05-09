package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;


public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Stage s;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		s = new Stage();
		s.addActor(new MiActor());

		Gdx.input.setInputProcessor(s);

		try{

			chatClient.clientSocket = new Socket("localhost", 6789);

			Thread t = new Thread(new MyServerListener());
			t.start();

			//chatClient.clientSocket.close();
		}
		catch(Exception e)
		{}
	}

	static public void EnviarServidor(String msj) {
		try {
			DataOutputStream outToServer =
					new DataOutputStream(
							chatClient.clientSocket.getOutputStream());

			outToServer.writeBytes(msj + '\n');
		}
		catch(Exception e)
		{}
	}
	@Override
	public void render () {
		s.draw();
		s.act();
	}
}
