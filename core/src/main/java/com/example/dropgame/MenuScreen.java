package com.example.dropgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MenuScreen implements Screen {

    private final Main game;

    private SpriteBatch batch;
    private BitmapFont font;
    private FitViewport viewport;

    public MenuScreen(Main game) {
        this.game = game;

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);

        viewport = new FitViewport(800, 500);
    }

    @Override
    public void render(float delta) {

        // Fondo marrón claro
        ScreenUtils.clear(0.82f, 0.70f, 0.55f, 1f);
        // (RGB aproximado de marrón claro)

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        font.draw(batch, "Android Devourer", 50f, 400f);
        font.draw(batch, "Llega hasta 30 para ganar", 50f, 360f);
        font.draw(batch, "Controles: flechas o ratón", 50f, 320f);
        font.draw(batch, "ENTER para empezar", 50f, 280f);

        batch.end();

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
