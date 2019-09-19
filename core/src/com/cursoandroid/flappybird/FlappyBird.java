package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import org.omg.PortableInterceptor.Interceptor;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	// classe para criar animações
	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoTopo;
	private Texture canoBaixo;
	private Texture gameOver;
	private Random numeroRandomico;
	private BitmapFont fonte;
	private  BitmapFont mensagem;
	private Circle passaroCirculo;
	private Rectangle retanguloCanoTopo;
	private Rectangle retanguloCanoBaixo;
	// desenha na tela as formas
	private ShapeRenderer shape; // USAR O SHAPE SOMENTE PARA DESENHAR FORMAS PARA TESTES

	// Atributos de COnfiguração
	private int movimento = 0;
	private int larguraDispositivo;
	private int alturaDispositivo;
	private int estadoJogo = 0; // 0-> jogo n iniciado; 1-> jogo iniciado; 2-> jogo gameover
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
		mensagem = new BitmapFont();

		passaroCirculo = new Circle();
		retanguloCanoTopo = new Rectangle();
		retanguloCanoBaixo = new Rectangle();
		//shape = new ShapeRenderer();


		fonte.setColor(Color.WHITE);
		fonte.getData().setScale(6);

		mensagem.setColor(Color.WHITE);
		mensagem.getData().setScale(3);


		// instancia para manipular as imagens
		batch = new SpriteBatch();
		//passaro = new Texture("passaro1.png");
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		canoTopo = new Texture("cano_topo_maior.png");
		canoBaixo = new Texture("cano_baixo_maior.png");
		gameOver = new Texture("game_over.png");


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

			velocidadeQueda++;
			if (posicaoInicialVertical > 0 || velocidadeQueda < 0) {
				posicaoInicialVertical -= velocidadeQueda;
			}

			if(estadoJogo == 1){

				//variacao += 0.1;
				// Diminuir variacao dos movimentos
				posicaoMovimentoCanoHorizontal -= deltaTime * 150;

				//velocidadeQueda += Gdx.graphics.getDeltaTime() * 5;
				// ver se a tela foi tocada para movimentar o passaro
				if (Gdx.input.justTouched()) {
					//Gdx.app.log("Toque", "Toque na tela");
					velocidadeQueda = -15;
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
			}else{ //Tela Game over

				// Resetando as posições
				if(Gdx.input.justTouched()){
					estadoJogo = 0;
					pontuacao = 0;
					velocidadeQueda = 0;
					posicaoInicialVertical = posicaoInicialVertical = alturaDispositivo / 2;
					posicaoMovimentoCanoHorizontal = larguraDispositivo;

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

		// Game over somente com estado de jogo 2
		if(estadoJogo == 2){
			batch.draw(gameOver, larguraDispositivo / 2 - gameOver.getWidth() / 2, alturaDispositivo / 2);
			mensagem.draw(batch, "Toque para reiniciar", larguraDispositivo / 2 - 200, alturaDispositivo / 2 - gameOver.getHeight());
		}

		movimento++;
		batch.end();

		// criado forma passaro
		passaroCirculo.set(120 + passaros[0].getWidth() / 2, posicaoInicialVertical + passaros[0].getHeight() / 2, 30 + passaros[0].getWidth() / 2);

		// criando forma canobaixo
		retanguloCanoBaixo = new Rectangle(
				posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica,
				canoBaixo.getWidth(), canoBaixo.getHeight()
		);

		// criando forma canotopo
		retanguloCanoTopo = new Rectangle(
				posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica,
				canoTopo.getWidth(), canoTopo.getHeight()
		);

		//Desenha as formas
		/*shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.circle(passaroCirculo.x, passaroCirculo.y, passaroCirculo.radius);
		shape.rect(retanguloCanoBaixo.x, retanguloCanoBaixo.y, retanguloCanoBaixo.width, retanguloCanoBaixo.height);
		shape.rect(retanguloCanoTopo.x, retanguloCanoTopo.y, retanguloCanoTopo.width, retanguloCanoTopo.height);
		shape.setColor(Color.RED);
		shape.end(); */

		// teste de colisao entre as formas
        if(Intersector.overlaps(passaroCirculo, retanguloCanoBaixo) || Intersector.overlaps(passaroCirculo, retanguloCanoTopo)
		|| posicaoInicialVertical <= 0 || posicaoInicialVertical >= alturaDispositivo ){
            //Gdx.app.log("colisao", "Houve uma colisao");

			estadoJogo = 2;

        }
	}

}
