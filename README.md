Documentación de proyecto de creación de un videojuego

El videojuego Android Devourer está organizado en dos pantallas principales.
Por un lado, una pantalla de menú, donde se muestran las instrucciones básicas y desde la que se inicia la partida. Y, por otro lado, una pantalla de juego, que contiene toda la mecánica: movimiento del jugador, generación de objetos, colisiones, puntuación, tiempo y condiciones de victoria o derrota.
Ambas clases siguen la estructura de pantallas de libGDX mediante la interfaz Screen.
MenuScreen
MenuScreen es la pantalla inicial del juego. Su propósito es presentar el título, explicar brevemente cómo se juega y permitir que el jugador comience la partida pulsando la tecla ENTER.
Elementos usados en MenuScreen:
    • Fondo: Se ha implementado un color marrón claro utilizando ScreenUtils.clear().
    • Texto en pantalla: Usando BitmapFont se muestran: 
    • El título del juego.
    • El objetivo principal.
    • Los controles disponibles.
    • Y la instrucción para empezar la partida.
    • Entrada del usuario: 
    • Se detecta la pulsación de ENTER mediante Gdx.input.isKeyJustPressed().
    • Al hacerlo, se cambia a la pantalla de juego (GameScreen).
Se han usado algunos recursos como SpriteBatch para dibujar el texto, BitmapFont con escala aumentada para mejorar la legibilidad de textos y FitViewport para mantener la proporción en cualquier resolución.
A su vez, se ha usado render() para dibujar el fondo y los textos, y comprobar si el jugador quiere empezar. También resize() para actualizar el viewport y dispose() para liberar los recursos gráficos usados.

GameScreen
La pantalla GameScreen contiene toda la lógica del juego. Aquí se gestionan el movimiento del jugador, la aparición de objetos, las colisiones, la puntuación, el temporizador y las condiciones de victoria o derrota.
Mecánicas implementadas
El movimiento del jugador es posible mediante las flechas arriba, abajo, izquierda y derecha. Y, además, mediante el uso del ratón, ya sea para arrastrar el modelo del jugador o pulsando en la pantalla.
Objetos del juego
La clase interna Item representa cada objeto y almacena:
    • Un Sprite.
    • Y un type que indica su función: 
    • 1: Manzana normal (+1 punto).
    • 2: Manzana dorada (+5 puntos).
    • 3: Bomba (Game Over).
Los objetos aparecen en posiciones aleatorias con estas probabilidades:
    • 60% manzana normal.
    • 20% manzana dorada.
    • 20% bomba (y además se generará una manzana normal extra ya que si únicamente generásemos una bomba no sería posible continuar la partida).
Para las colisiones se han usado rectángulos para comprobar si el jugador toca un objeto.
Cuando ocurre una colisión:
    • Se reproduce un sonido.
    • Se suma la puntuación correspondiente (o se activa el Game Over si es una bomba).
    • Y se genera un nuevo objeto.
Las condiciones son las siguientes, para poder ganar, es decir, para conseguir la victoria al jugador será necesario que itemsCollected sea mayor igual a 30. En cambio, la derrota se dará en caso de tocar una bomba o de que se agote el tiempo.
Interfaz gráfica (UI)
En pantalla se han añadido:
    • El tiempo restante (arriba a la derecha).
    • La puntuación (arriba a la izquierda).
    • Y un mensaje de GAME OVER o ¡HAS GANADO! que aparecerán centrados en la pantalla dependiendo del caso.
Se han usado diferentes texturas descargadas desde Internet para el fondo, el jugador, las manzanas y la bomba. Además, se ha implementado un sonido al momento de recoger un objeto, música de fondo en bucle. Y el SpriteBatch para dibujar tanto el mundo como la interfaz.
Finalmente para esta clase se han usado los métodos input() para gestionar los controles del jugador, logic() para actualizar el tiempo, las colisiones y la puntuación, draw() para dibujar el escenario, los objetos y la interfaz. Y dispose() para liberar todos los recursos utilizados.
