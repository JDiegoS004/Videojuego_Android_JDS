package com.example.dropgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {

    private final Main game;

    Texture Texture1;
    Texture Texture2;
    Texture Texture3;
    Texture backgroundTexture;
    Texture bucketTexture;
    Sound dropSound;
    Music backgroundMusic;
    FitViewport viewport;
    FitViewport uiViewport;
    SpriteBatch batch;
    Sprite bucketSprite;
    Vector2 touchpos;

    // Clase interna para guardar el "tipo" de objeto junto con el Sprite
    class Item {
        Sprite sprite;
        int type; // 1 = Manzana Normal (+1), 2 = Manzana Dorada (+5), 3 = Bomba (GameOver)

        Item(Sprite sprite, int type) {
            this.sprite = sprite;
            this.type = type;
        }
    }

    Array<Item> Items;
    Rectangle bucketRectangle;
    Rectangle dropRectangle;

    float timeLeft = 30f;
    int itemsCollected = 0;
    boolean gameOver = false;
    boolean gameWon = false;

    BitmapFont font;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void dispose() {
        if (Texture1 != null)
            Texture1.dispose();
        if (Texture2 != null)
            Texture2.dispose();
        if (Texture3 != null)
            Texture3.dispose();
        if (backgroundTexture != null)
            backgroundTexture.dispose();
        if (bucketTexture != null)
            bucketTexture.dispose();
        if (dropSound != null)
            dropSound.dispose();
        if (backgroundMusic != null)
            backgroundMusic.dispose();
        if (batch != null)
            batch.dispose();
        if (font != null)
            font.dispose();
    }

    @Override
    public void show() {
        backgroundTexture = new Texture("background.png");
        Texture1 = new Texture("item.png");
        Texture2 = new Texture("item2.png");
        Texture3 = new Texture("item3.png");
        bucketTexture = new Texture("modelo_videojuego.png");

        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

        batch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
        uiViewport = new FitViewport(800, 500);

        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(1, 1);

        touchpos = new Vector2();
        Items = new Array<>();

        bucketRectangle = new Rectangle();
        dropRectangle = new Rectangle();

        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(.5f);
        backgroundMusic.play();

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);

        createItems();
    }

    public void createItems() {
        float dropWidth = 1;
        float dropHeight = 1;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        int type = 1;
        Texture tex = Texture1;
        float randomVal = MathUtils.random();

        if (randomVal > 0.8f) {
            type = 3;
            tex = Texture3;
        } else if (randomVal > 0.6f) {
            type = 2;
            tex = Texture2;
        }

        Sprite dropSprite = new Sprite(tex);
        dropSprite.setSize(dropWidth, dropHeight);
        dropSprite.setX(MathUtils.random(0f, worldWidth - dropWidth));
        dropSprite.setY(MathUtils.random(0f, worldHeight - dropHeight));

        Items.add(new Item(dropSprite, type));

        if (type == 3) {
            Sprite extraSprite = new Sprite(Texture1);
            extraSprite.setSize(dropWidth, dropHeight);
            extraSprite.setX(MathUtils.random(0f, worldWidth - dropWidth));
            extraSprite.setY(MathUtils.random(0f, worldHeight - dropHeight));

            Items.add(new Item(extraSprite, 1));
        }
    }

    @Override
    public void render(float delta) {
        if (!gameOver && !gameWon) {
            input();
            logic();
        }
        draw();
    }

    public void input() {
        float speed = 3f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            bucketSprite.translateX(speed * delta);
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            bucketSprite.translateX(-speed * delta);
        else if (Gdx.input.isKeyPressed(Input.Keys.UP))
            bucketSprite.translateY(speed * delta);
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            bucketSprite.translateY(-speed * delta);

        if (Gdx.input.isTouched()) {
            touchpos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchpos);
            bucketSprite.setCenterX(touchpos.x);
            bucketSprite.setCenterY(touchpos.y);
        }
    }

    public void logic() {
        float delta = Gdx.graphics.getDeltaTime();

        timeLeft -= delta;
        if (timeLeft <= 0) {
            timeLeft = 0;
            gameOver = true;
            return;
        }

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();

        bucketSprite.setY(MathUtils.clamp(bucketSprite.getY(), 0, worldHeight - bucketHeight));
        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth - bucketWidth));

        bucketRectangle.set(bucketSprite.getX(), bucketSprite.getY(), bucketWidth, bucketHeight);

        for (int i = Items.size - 1; i >= 0; i--) {
            Item item = Items.get(i);
            Sprite dropSprite = item.sprite;

            dropRectangle.set(dropSprite.getX(), dropSprite.getY(),
                    dropSprite.getWidth(), dropSprite.getHeight());

            if (bucketRectangle.overlaps(dropRectangle)) {
                dropSound.play();
                Items.removeIndex(i);

                if (item.type == 1) {
                    itemsCollected += 1;
                } else if (item.type == 2) {
                    itemsCollected += 5;
                } else if (item.type == 3) {
                    gameOver = true;
                    return;
                }

                if (itemsCollected >= 30) {
                    gameWon = true;
                    return;
                }

                createItems();
            }
        }
    }

    public void draw() {
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        bucketSprite.draw(batch);

        for (Item item : Items)
            item.sprite.draw(batch);
        batch.end();

        uiViewport.apply();
        batch.setProjectionMatrix(uiViewport.getCamera().combined);
        batch.begin();

        float uiWorldWidth = uiViewport.getWorldWidth();
        float uiWorldHeight = uiViewport.getWorldHeight();

        font.draw(batch, "Tiempo: " + (int) Math.ceil(timeLeft), uiWorldWidth - 180f, uiWorldHeight - 20f);

        font.draw(batch, "Puntos: " + itemsCollected, 20f, uiWorldHeight - 20f);

        if (gameOver) {
            font.draw(batch, "GAME OVER", uiWorldWidth / 2 - 80f, uiWorldHeight / 2);
        } else if (gameWon) {
            font.draw(batch, "¡HAS GANADO!", uiWorldWidth / 2 - 90f, uiWorldHeight / 2);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        if (uiViewport != null) {
            uiViewport.update(width, height, true);
        }
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
}
