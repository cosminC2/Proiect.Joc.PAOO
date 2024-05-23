package PaooGame.Misc.GameState;

import PaooGame.Misc.KeyHandler;

;
public class GameState {
    //state machine pentru determinare jocului
    //Title -> Menu
    //          /\
    //         /  \
    //  Settings   Game
    //Game -> Title
    //Settings -> Menu
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
            kh.spacePressed = false;
            switch(state)
            {
                case TitleScreen:
                    state = State.MainMenu;
                    break;
                case MainMenu:
                case Settings:
                    state = State.Game;
                    break;
            }
        }
    }
}
