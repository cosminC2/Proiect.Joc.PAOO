package PaooGame.Misc.GameState;

import PaooGame.Misc.KeyHandler;

;
public class GameState {
    State state;
    public GameState(State state)
    {
        this.state=state;
    }

    public State getState(){return state;}

    public void setState(State state) {
        this.state = state;
    }

    public void performUpdate(KeyHandler kh)
    {
        if(kh.spacePressed)
        {
            switch(state)
            {
                case TitleScreen:
                    state = State.Game;
                    break;

            }
        }
    }
}
