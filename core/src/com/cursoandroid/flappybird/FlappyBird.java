package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {

	// classe para criar animações
	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;

	// Atributos de COnfiguração
	private int movimento = 0;
	private int larguraDispositivo;
	private int alturaDispositivo;

	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaoInicialVertical;

	
	@Override
	public void create () {

		// instancia para manipular as imagens
		batch = new SpriteBatch();
		//passaro = new Texture("passaro1.png");
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		fundo = new Texture("fundo.png");

        larguraDispositivo = Gdx.graphics.getWidth();
        alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVertical = alturaDispositivo / 2;

	}

	@Override
	public void render () {

		//variacao += 0.1;
		// Diminuir variacao dos movimentos
		variacao += Gdx.graphics.getDeltaTime() * 3;
		//velocidadeQueda++;
		velocidadeQueda += Gdx.graphics.getDeltaTime() * 5;

		if(variacao > 2){
			variacao = 0;
		}

		if(posicaoInicialVertical > 0){
			posicaoInicialVertical -= velocidadeQueda;
		}
		
		// iniciar exibição das imagens
		batch.begin();

		batch.draw(fundo, 0,0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int)variacao],30,posicaoInicialVertical);
		movimento++;

		batch.end();
	}

}
