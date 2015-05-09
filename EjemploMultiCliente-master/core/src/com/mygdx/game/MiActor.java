package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by turupawn on 4/29/15.
 */
public class MiActor extends Image
{
    MiActor()
    {
        super(new Texture("badlogic.jpg"));

        addListener(new ClickListener()
        {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
            {
                super.touchDown(event, x, y, pointer,button);
                MyGdxGame.EnviarServidor("TouchDown");
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
        setX(getX() + 1);
    }
}
