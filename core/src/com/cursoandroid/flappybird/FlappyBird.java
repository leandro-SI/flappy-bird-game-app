package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	// classe para criar animações
	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoTopo;
	private Texture canoBaixo;
	private Random numeroRandomico;
	private BitmapFont fonte;

	// Atributos de COnfiguração
	private int movimento = 0;
	private int larguraDispositivo;
	private int alturaDispositivo;
	private int estadoJogo = 0; // Iniciado ou não iniciado
    private int pontuacao = 0;

	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaoInicialVertical;
	private float posicaoMovimentoCanoHorizontal;
	private float espacoEntreCanos;
	private float deltaTime;
	private float alturaEntreCanosRandomica;
	private  boolean marcouPonto = false;

	
	@Override
	public void create () {

		numeroRandomico = new Random();

		fonte = new BitmapFont();
		fonte.setColor(Color.WHITE);
		fonte.getData().setScale(6);

		// instancia para manipular as imagens
		batch = new SpriteBatch();
		//passaro = new Texture("passaro1.png");
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		canoTopo = new Texture("cano_topo_maior.png");
		canoBaixo = new Texture("cano_baixo_maior.png");


		fundo = new Texture("fundo.png");

        larguraDispositivo = Gdx.graphics.getWidth();
        alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVertical = alturaDispositivo / 2;
		posicaoMovimentoCanoHorizontal = larguraDispositivo - 100;
		espacoEntreCanos = 400;

	}

	@Override
	public void render () {

		deltaTime = Gdx.graphics.getDeltaTime();
		variacao += deltaTime * 3;

		if (variacao > 2) {
			variacao = 0;
		}

		if(estadoJogo == 0){ // Não inciiado

			if(Gdx.input.justTouched()){
				estadoJogo = 1;
			}
		}else {




			//variacao += 0.1;
			// Diminuir variacao dos movimentos


			posicaoMovimentoCanoHorizontal -= deltaTime * 150;

			velocidadeQueda++;
			//velocidadeQueda += Gdx.graphics.getDeltaTime() * 5;

			// ver se a tela foi tocada para movimentar o passaro
			if (Gdx.input.justTouched()) {
				//Gdx.app.log("Toque", "Toque na tela");
				velocidadeQueda = -15;
			}

			if (posicaoInicialVertical > 0 || velocidadeQueda < 0) {
				posicaoInicialVertical -= velocidadeQueda;
			}

			// Verifica se cano saiu da tela
			if (posicaoMovimentoCanoHorizontal < -100) {
				posicaoMovimentoCanoHorizontal = larguraDispositivo;
				alturaEntreCanosRandomica = numeroRandomico.nextInt(400) - 200;
				marcouPonto = false;
			}

			//Verifica pontuacao
			if(posicaoMovimentoCanoHorizontal < 120){

				if(marcouPonto ==  false){
					pontuacao++;
					marcouPonto = true;
				}

			}


		}

		// iniciar exibição das imagens
		batch.begin();

		batch.draw(fundo, 0,0, larguraDispositivo, alturaDispositivo);
		batch.draw(canoTopo, posicaoMovimentoCanoHorizontal,alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica);
		batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica);
		batch.draw(passaros[(int)variacao],120,posicaoInicialVertical);
		fonte.draw(batch, String.valueOf(pontuacao), larguraDispositivo / 2, alturaDispositivo - 50);
		movimento++;

		batch.end();
	}

}
