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
    public static BufferedImage cursor;
    public static BufferedImage hover;
    public static BufferedImage attack;
    public static BufferedImage mainMenu;
    public static BufferedImage gameOverScreen;
    public static BufferedImage imgBase;
    public static BufferedImage[] TileList = new BufferedImage[32*32];
    public static BufferedImage[] CharacterImages;
    public static BufferedImage imeMenuBase;
    public static BufferedImage imgMenu;

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
        imgBase = ImageLoader.LoadImage("/textures/PaooHoverMenuBase.png");
        imeMenuBase = ImageLoader.LoadImage("/textures/PaooCharMenuBase.png");
        imgMenu = ImageLoader.LoadImage("/textures/PaooMenuSprite.png");
        CharacterImages = new BufferedImage[9];
        CharacterImages[0]= ImageLoader.LoadImage("/textures/PaooBadGuySprite.png");
        CharacterImages[1]= ImageLoader.LoadImage("/textures/PaooBossSprite.png");
        CharacterImages[2]= ImageLoader.LoadImage("/textures/PaooColmSprite.png");
        CharacterImages[3]= ImageLoader.LoadImage("/textures/PaooErikaSprite.png");
        CharacterImages[4]= ImageLoader.LoadImage("/textures/PaooFranzSprite.png");
        CharacterImages[5]= ImageLoader.LoadImage("/textures/PaooGarciaSprite.png");
        CharacterImages[6]= ImageLoader.LoadImage("/textures/PaooGiliamSprite.png");
        CharacterImages[7]= ImageLoader.LoadImage("/textures/PaooLuteSprite.png");
        CharacterImages[8]= ImageLoader.LoadImage("/textures/PaooSethSprite.png");
    }
    public static BufferedImage getCharacterImage(String name){
        switch(name){
            case "BadGuy": return CharacterImages[0];
            case "Boss": return CharacterImages[1];
            case "Colm": return CharacterImages[2];
            case "Erika": return CharacterImages[3];
            case "Franz": return CharacterImages[4];
            case "Garcia": return CharacterImages[5];
            case "Giliam": return CharacterImages[6];
            case "Lute": return CharacterImages[7];
            case "Seth": return CharacterImages[8];
            default: return null;
        }
    }
}
