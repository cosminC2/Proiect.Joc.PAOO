package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

public class CursorTile extends Tile
{
    public CursorTile(int id)
    {
        /// Apel al constructorului clasei de baza
        super(Assets.cursor, id, false);
    }
}
