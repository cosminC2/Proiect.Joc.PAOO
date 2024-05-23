package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

/*! \class public class GrassTile extends Tile
    \brief Abstractizeaza notiunea de dala de tip iarba.
 */
public class UniversalTile extends Tile
{
    /*! \fn public GrassTile(int id)
        \brief Constructorul de initializare al clasei

        \param id Id-ul dalei util in desenarea hartii.
     */
    public UniversalTile(int id)
    {
        /// Apel al constructorului clasei de baza

        //in loc de a crea 2,000 de clase diferite, generalizez procesul si creez o clasa universala pentru asta
        super(Assets.TileList[id], id, false);
    }
}
