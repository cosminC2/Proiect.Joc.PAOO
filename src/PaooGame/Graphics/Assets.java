package PaooGame.Graphics;

import PaooGame.Tiles.Tile;

import java.awt.image.BufferedImage;

/*! \class public class Assets
    \brief Clasa incarca fiecare element grafic necesar jocului.

    Game assets include tot ce este folosit intr-un joc: imagini, sunete, harti etc.
 */
public class Assets
{
        /// Referinte catre elementele grafice (dale) utilizate in joc.
    public static BufferedImage playerLeft;
    public static BufferedImage playerRight;
    public static BufferedImage soil;
    public static BufferedImage grass;
    public static BufferedImage mountain;
    public static BufferedImage townGrass;
    public static BufferedImage townGrassDestroyed;
    public static BufferedImage townSoil;
    public static BufferedImage water;
    public static BufferedImage rockUp;
    public static BufferedImage rockDown;
    public static BufferedImage rockLeft;
    public static BufferedImage rockRight;
    public static BufferedImage tree;
    public static BufferedImage cursor;
    public static BufferedImage hover;
    public static BufferedImage attack;
    public static BufferedImage mainMenu;
    public static BufferedImage gameOverScreen;
    public static BufferedImage[] TileList = new BufferedImage[32*32];

    /*! \fn public static void Init()
        \brief Functia initializaza referintele catre elementele grafice utilizate.

        Aceasta functie poate fi rescrisa astfel incat elementele grafice incarcate/utilizate
        sa fie parametrizate. Din acest motiv referintele nu sunt finale.
     */
    public static void Init()
    {
            /// Se creaza temporar un obiect SpriteSheet initializat prin intermediul clasei ImageLoader
        SpriteSheet sheet = new SpriteSheet(ImageLoader.LoadImage("/textures/PaooGameSpriteSheet.png"));

            /// Se obtin subimaginile corespunzatoare elementelor necesare.
        for(int x=0;x<32;x++) {
            for (int y = 0; y < 32; y++)
            {
                TileList[y*32+x]=sheet.crop(x,y);
            }
        }
        cursor = sheet.crop(7,1);
        hover = sheet.crop(15,1);
        attack = sheet.crop(16,1);
        //DE REFACUT APPROXIMATIV TOT
        //MUIE TILE SHEETS
        //defapt, ar trebui sa refac sistemul intreg SA MA LASE SA FAC DOUA SUTE TILE-URI
        //ok dar fiecare tile ii o clasa in sine
        //OH I KNOW
        //https://i.imgur.com/1JMp4Vd.png
        //gandeste tu ceva cand nu ii ora 2 noaptea

        mainMenu = ImageLoader.LoadImage("/textures/PaooTitleScreen.png");
        gameOverScreen = ImageLoader.LoadImage("/textures/PaooGameOverScreen.png");
    }
}
