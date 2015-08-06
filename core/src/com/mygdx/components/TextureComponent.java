package com.mygdx.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

public class TextureComponent extends Component {
	//unnoetig
        public Texture txt;
        public TextureComponent(Texture txt){
            this.txt = txt;
        }
		
}
