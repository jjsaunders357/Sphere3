package com.pheiffware.sphere3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pheiffware.lib.and.gui.graphics.openGL.BaseGameFragment;
import com.pheiffware.lib.and.gui.graphics.openGL.GameView;
import com.pheiffware.lib.graphics.FilterQuality;

/**
 * Created by Steve on 7/7/2017.
 */

public class Sphere3Fragment extends BaseGameFragment
{
    @Override
    public GameView onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return new GameView(getContext(), new Sphere3Renderer(), FilterQuality.MEDIUM, false, true);
    }
}
