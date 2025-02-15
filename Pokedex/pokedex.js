"use strict";

/**
 * Rachel Benincasa
 * 05/11/23
 * TAs: Rasmus & Wen
 * This is the JS file for the Pokemon battle game for HW3. In this file, the infomation
 * is given from the PokeAPI to get info about pokemon. It also includes post requests to
 * run individual moves and update the game accordingly.
 */

(function() {
  const URL = "https://courses.cs.washington.edu/courses/cse154/webservices/pokedex/";
  const POKEDEX_URL = URL + "pokedex.php";
  const SPRITES_URL = URL + "sprites/";
  const GAME_URL = URL + "game.php";
  const STARTER_POKEMON = ["bulbasaur", "charmander", "squirtle"];
  const MAX_MOVES = 4;
  const PERCENT = 100;
  const LOW_HEALTH_PERCENT = 0.2;

  let myPokemonName;
  let myPokemonHealth;
  let myPokemon;
  let opponentPokemon;
  let guid;
  let pid;

  window.addEventListener("load", init);

  /**
   * Runs when the page first loads.
   */
  function init() {
    id("start-btn").classList.add("hidden");
    id("start-btn").addEventListener("click", startGame);
    id("flee-btn").addEventListener("click", fleeBattle);
    id("endgame").addEventListener("click", endGame);
    for (let i = 0; i < qsa("#p1 .moves button").length; i++) {
      qsa("#p1 .moves button")[i].addEventListener("click", makeMove);
      for (let j = 0; j < qsa("#p1 .moves button").length; j++) {
        qsa("#p1 .moves button")[j].disabled = true;
      }
    }
    showAllPokemon();
  }

  /**
   * Displays all the pokemon in the pokedex
   */
  function showAllPokemon() {
    fetch(POKEDEX_URL + "?pokedex=all")
      .then(statusCheck)
      .then(data => data.text())
      .then(displayAllImgs)
      .catch(console.error);
  }

  /**
   * Adjusts the screen to prepare for a battle. Uses a post request to
   * get game information and get the opponents pokemon stats.
   */
  function startGame() {
    id("pokedex-view").classList.add("hidden");
    id("p2").classList.remove("hidden");
    qs(".hp-info").classList.remove("hidden");
    id("results-container").classList.remove("hidden");
    id("flee-btn").classList.remove("hidden");
    id("start-btn").classList.add("hidden");
    qs("h1").textContent = "Pokemon Battle!";
    for (let i = 0; i < qsa("#p1 button").length; i++) {
      qsa("#p1 button")[i].disabled = false;
    }
    myPokemonHealth = myPokemon.hp;

    let myPokemonData = new FormData();
    myPokemonData.append("startgame", "true");
    myPokemonData.append("mypokemon", myPokemonName);
    fetch(GAME_URL, {method: "POST", body: myPokemonData})
      .then(statusCheck)
      .then(data => data.json())
      .then((res) => {
        getPokemonStats(res, "#p2");
      })
      .catch(console.error);
  }

  /**
   * Called when the player plays a move. Uses string methods to grab
   * the move name to call to the fetchMove function.
   */
  function makeMove() {
    id("loading").classList.remove("hidden");
    let moveData = new FormData();
    moveData.append("guid", guid);
    moveData.append("pid", pid);

    let move = this.textContent.trim().split(" ");
    let name = "";
    if (move.length > 1) {
      for (let i = 0; i < move.length - 2; i++) {
        name += move[i];
      }
    } else {
      name = move[0];
    }
    name = name.toLowerCase();
    moveData.append("movename", name);
    fetchMove(moveData);
  }

  /**
   * Uses a POST request to fetch moves for both players and adjust the game accordingly.
   * @param {FormData} movetype - hold parameters for the POST request
   */
  function fetchMove(movetype) {
    fetch(GAME_URL, {method: "POST", body: movetype})
      .then(statusCheck)
      .then(data => data.json())
      .then(gameplay)
      .catch(console.error);
  }

  /**
   * Gathers each move and hit/miss status and displays them on the screen.
   * @param {Object} move - JSON object holding the contents of the POST request.
   */
  function gameplay(move) {
    id("loading").classList.add("hidden");
    let p1move = move.results["p1-move"];
    let p2move = move.results["p2-move"];
    let p1result = move.results["p1-result"];
    let p2result = move.results["p2-result"];

    id("p1-turn-results").classList.remove("hidden");
    id("p2-turn-results").classList.remove("hidden");

    checkHealth(move, "p1");
    checkHealth(move, "p2");

    id("p1-turn-results").textContent = "Player 1 played " + p1move + " and " + p1result + "!";
    if (p1move !== "flee") {
      id("p2-turn-results").textContent = "Player 2 played " + p2move + " and " + p2result + "!";
    }
  }

  /**
   * Checks the health of a given player and adjusts the status of the game based on their health.
   * @param {Object} move - JSON object holding contents of POST request
   * @param {String} player - either "p1" (user) or "p2" (opponent)
   */
  function checkHealth(move, player) {
    let currentHP = move[player]["current-hp"];
    let totalHP = move[player]["hp"];
    let healthbar = "#" + player + " .health-bar";
    if (player === "p1") {
      myPokemon.hp = currentHP;
    } else {
      opponentPokemon.hp = currentHP;
    }
    qs(healthbar).style.width = (PERCENT * (currentHP / totalHP)) + "%";
    qs("#" + player + " .hp").textContent = currentHP + "HP";
    if (currentHP / totalHP <= LOW_HEALTH_PERCENT) {
      qs(healthbar).classList.add("low-health");
    }

    if (currentHP === 0) {
      for (let i = 0; i < qsa("#p1 .moves button").length; i++) {
        qsa("#p1 .moves button")[i].disabled = true;
      }
      if (player === "p1") {
        qs("h1").textContent = "You lost!";
      } else {
        qs("h1").textContent = "You won!";
        id("p2-turn-results").classList.add("hidden");
        makeFound(opponentPokemon.shortname, "#p1");
      }
      id("flee-btn").classList.add("hidden");
      id("endgame").classList.remove("hidden");
    }
  }

  /**
   * Adjusts the website to revert to its original state after a game ends. Removes all
   * pieces from the battle.
   */
  function endGame() {
    id("endgame").classList.add("hidden");
    id("p1-turn-results").textContent = "";
    id("p2-turn-results").textContent = "";
    id("results-container").classList.add("hidden");
    id("p2").classList.add("hidden");
    id("start-btn").classList.remove("hidden");
    qs("h1").textContent = "Your Pokedex";
    id("pokedex-view").classList.remove("hidden");
    qs("#p1 .hp").textContent = myPokemonHealth + "HP";
    qs("#p1 .health-bar").style.width = "100%";
    qs("#p1 .health-bar").classList.remove("low-health");
    qs("#p2 .health-bar").style.width = "100%";
    qs("#p2 .health-bar").classList.remove("low-health");
  }

  /**
   * Called when the flee button is pressed. Adjusts game based on flee request.
   */
  function fleeBattle() {
    let fleeData = new FormData();
    fleeData.append("guid", guid);
    fleeData.append("pid", pid);
    fleeData.append("movename", "flee");
    fetchMove(fleeData);
  }

  /**
   * Using a list of pokemon, displays them in the pokedex as not found.
   * @param {Text} data - text of all the pokemon in Name:shortname format
   */
  function displayAllImgs(data) {
    let allPokemon = data.split("\n");
    for (let i = 0; i < allPokemon.length; i++) {
      let currentPokemon = allPokemon[i];
      let fullname = currentPokemon.split(":")[0];
      let shortname = currentPokemon.split(":")[1];
      let currentSprite = document.createElement("img");
      currentSprite.id = shortname;
      currentSprite.src = SPRITES_URL + shortname + ".png";
      currentSprite.alt = "A sprite of the pokemon " + fullname;
      currentSprite.classList.add("sprite");
      id("pokedex-view").appendChild(currentSprite);
      if (STARTER_POKEMON.includes(shortname)) {
        makeFound(shortname);
      }
    }
  }

  /**
   * Given a string containing a pokemon name, makes them found and able to be used in battle.
   * @param {String} pokemon - pokemon to be found.
   */
  function makeFound(pokemon) {
    id(pokemon).classList.add("found");
    id(pokemon).addEventListener("click", () => {
      fetch(POKEDEX_URL + "?pokemon=" + pokemon)
        .then(statusCheck)
        .then(data => data.json())
        .then((data) => {
          getPokemonStats(data, "#p1");
        })
        .catch(console.error);
    });
  }

  /**
   * Given the data of a certain pokemon, and if the pokemon is for the user or for
   * the opponent, gets and displays the stats of the specific pokemon.
   * @param {Object} data - JSON Object of a specific pokemon
   * @param {String} pokemon - either "#p1" (user) or "#p2" (opponent)
   */
  function getPokemonStats(data, pokemon) {
    if (pokemon === "#p2") {
      guid = data.guid;
      pid = data.pid;
      data = data.p2;
      opponentPokemon = data;
    } else {
      id("start-btn").classList.remove("hidden");
      myPokemon = data;
    }
    qs(pokemon + " .name").textContent = data.name;
    let pokemonImg = qs(pokemon + " .pokepic");
    pokemonImg.src = URL + data.images.photo;
    pokemonImg.alt = data.name;
    qs(pokemon + " .hp").textContent = data.hp + "HP";
    qs(pokemon + " .info").textContent = data.info.description;
    let typeImage = qs(pokemon + " .type");
    let weaknessImage = qs(pokemon + " .weakness");
    getImages(typeImage, data.images.typeIcon, data.info.type);
    getImages(weaknessImage, data.images.weaknessIcon, data.info.weakness);
    getMoves(data, pokemon);
    myPokemonName = data.shortname;
  }

  /**
   * Gets the moves of a specific pokemon
   * @param {*} data - JSON Object of a specific pokemon
   * @param {*} pokemon - Either "#p1" (user) or "#p2" (opponent)
   */
  function getMoves(data, pokemon) {
    let numMoveButtons = qsa(pokemon + " .moves button");
    addMoves(data, pokemon);
    hideUnusedMoves(data, numMoveButtons);
  }

  /**
   * Displays the moves for a specific pokemon
   * @param {Object} data - JSON Object that holds Pokemon info
   * @param {String} pokemon - either "#p1" (user) or ()
   */
  function addMoves(data, pokemon) {
    let numMoveButtons = qsa(pokemon + " .moves button");
    for (let i = 0; i < MAX_MOVES; i++) {
      numMoveButtons[i].classList.remove("hidden");
    }
    let currMoves = qsa(pokemon + " .move");
    let currIcons = qsa(pokemon + " .moves img");
    for (let i = 0; i < data.moves.length; i++) {
      let dataName = data.moves[i].name;
      let dataType = data.moves[i].type;
      currMoves[i].textContent = dataName;
      getImages(currIcons[i], "icons/" + dataType + ".jpg", dataType);
      if (data.moves[i].dp) {
        qsa(pokemon + " .dp")[i].textContent = data.moves[i].dp + " DP";
      } else {
        qsa(pokemon + " .dp")[i].textContent = "";
      }
    }
  }

  /**
   * Hides the remaining move boxes if the pokemon has less than 4 moves.
   * @param {Object} data - JSON Object holding Pokemon info
   * @param {Array} num - array containing pokemon moves slots.
   */
  function hideUnusedMoves(data, num) {
    for (let i = data.moves.length; i < MAX_MOVES; i++) {
      num[i].classList.add("hidden");
    }
  }

  /**
   * Gets the SRC and ALT for an image of a pokemon
   * @param {String} currImg - current pokemon image
   * @param {String} pokemonIcon - String holding pokemons icon
   * @param {String} pokemonType - String holding pokemons type
   */
  function getImages(currImg, pokemonIcon, pokemonType) {
    currImg.src = URL + pokemonIcon;
    currImg.alt = pokemonType + " type";
  }

  /**
   * Checks the status of the fetch call and catches errors.
   * @param {Response} res - response from GET/POST fetch call
   * @returns {Response} - response from GET/POST fetch call
   */
  async function statusCheck(res) {
    if (!res.ok) {
      throw new Error(await res.text());
    }
    return res;
  }

  /**
   * Returns an object that holds the specific ID from the HTML file.
   * @param {string} id - HTML Object with ID
   * @returns {object} - The value that is held by the ID the user wants.
   */
  function id(id) {
    return document.getElementById(id);
  }

  /**
   * Returns the first element that matches the given query.
   * @param {string} selector - CSS query selector
   * @returns {object} - The value of the first match. If no match returns null.
   */
  function qs(selector) {
    return document.querySelector(selector);
  }

  /**
   * Returns an array of elements matching the given query.
   * @param {string} selector - CSS query selector.
   * @returns {array} - Array of DOM objects matching the given query.
   */
  function qsa(selector) {
    return document.querySelectorAll(selector);
  }

})();
