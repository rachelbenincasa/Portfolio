// Login java script that takes care of all the authentification and forget password.

//function for adding a user
function setupRequestPostAddUser() {
    var httpRequest = new XMLHttpRequest();
    if (!httpRequest) {
    alert('Error!');
    return false;
    }
    httpRequest.onreadystatechange = () => {
    if (httpRequest.readyState === XMLHttpRequest.DONE) {
    if (httpRequest.status === 200) {
        if (httpRequest.responseText.startsWith("USER")){
            alert(httpRequest.responseText);
         }
         else {
             document.getElementById("message").innerText = httpRequest.responseText;
         }
    } else { alert('ERROR'); }
    }
    }
    body = { //creating message object
         username: document.getElementById("newusername").value,
         fullName: document.getElementById("fullname").value,
         password: document.getElementById("newpassword").value,
         question: document.getElementById("securityQuestion").value,
         answer: document.getElementById("securityanswer").value,
    }
    //the post request for the user inputs of username and password
    let url = '/add/user/';
    httpRequest.open('POST', url);
    httpRequest.setRequestHeader('Content-Type','application/json');
    httpRequest.send(JSON.stringify(body));
    document.getElementById("newusername").value = '';
    document.getElementById("fullname").value = "";
    document.getElementById("newpassword").value = "";
    document.getElementById("securityQuestion").value = "";
    document.getElementById("securityanswer").value = "";
   }

   //function for login
   function setupRequestPostLogin() {
    var httpRequest = new XMLHttpRequest();
    if (!httpRequest) {
    alert('Error!');
    return false;
    }
    httpRequest.onreadystatechange = () => {
    if (httpRequest.readyState === XMLHttpRequest.DONE) {
    if (httpRequest.status === 200) {
        if (httpRequest.responseText.startsWith("SUCCESS")){
            // WE NEED TO DETERMINE A LANDING PAGE FOR WHEN LOGIN SUCCEEDS
           window.location.href = '/index.html';
        }
        else if (httpRequest.responseText.startsWith('/')){
            window.location.href = httpRequest.responseText;
        }
        else {
            alert(httpRequest.responseText);
        }
    console.log('successfully proccessed');
    } else { alert('ERROR'); }
    }
    }
    body = { //creating message object
         username: document.getElementById("loginusername").value,
         password: document.getElementById("loginpassword").value,

    }
    //the post request for the user inputs of username and password
    let url = '/login/';
    httpRequest.open('POST', url);
    httpRequest.setRequestHeader('Content-Type','application/json');
    httpRequest.send(JSON.stringify(body));
    document.getElementById("loginusername").value = "";
    document.getElementById("loginpassword").value = "";
   }

   //TODO: function for forgot password
   function setupRequestPostForgotPassword() {
    var httpRequest = new XMLHttpRequest();
    if (!httpRequest) {
    alert('Error!');
    return false;
    }
    httpRequest.onreadystatechange = () => {
    if (httpRequest.readyState === XMLHttpRequest.DONE) {
    if (httpRequest.status === 200) {
        if (httpRequest.responseText.startsWith("SUCCESS")){
            console.log('hello');
            // If security question is answered correctly then a new input field would be visible
           document.getElementById("createPassword").innerHTML = "<label for='password'> New Password </label><input id='password'>"
           + "<button id='submitPassword' onclick = 'setupRequestPostUpdatePassword()'>Update Password</button>";
           document.getElementById("createPassword").style.display = 'block';
        }
        else if (httpRequest.responseText.startsWith('/')){
            window.location.href = httpRequest.responseText;
        }
        else {
            alert(httpRequest.responseText);
        }
    } else { alert('ERROR'); }
    }
    }
    body = { //creating message object
        username: document.getElementById("forgotpasswordUsername").value,
         question: document.getElementById("securityQuestion").value,
         answer: document.getElementById("securityanswer").value

    }
    //the post request for the user inputs of username and password
    let url = '/forgot/';
    httpRequest.open('POST', url);
    httpRequest.setRequestHeader('Content-Type','application/json');
    httpRequest.send(JSON.stringify(body));
    document.getElementById("securityanswer").value = "";
   }
   //Function for update password
   function setupRequestPostUpdatePassword() {
    var httpRequest = new XMLHttpRequest();
    if (!httpRequest) {
    alert('Error!');
    return false;
    }
    httpRequest.onreadystatechange = () => {
    if (httpRequest.readyState === XMLHttpRequest.DONE) {
    if (httpRequest.status === 200) {
        if (httpRequest.responseText.startsWith("SUCCESS")){
            // If creating a password is successful then user will be redirected back to signin.html;
            window.location.href = '/signin.html';
        }
        else if (httpRequest.responseText.startsWith('/')){
            window.location.href = httpRequest.responseText;
        }
        else {
            alert(httpRequest.responseText);
        }
    } else { alert('ERROR'); }
    }
    }
    body = { //creating message object
         password: document.getElementById("password").value,

    }

    let url = '/update/password';
    httpRequest.open('POST', url);
    httpRequest.setRequestHeader('Content-Type','application/json');
    httpRequest.send(JSON.stringify(body));
   }


// rachel's work below


// add new movie to database
function addNewMovie() {
    let newMovieStats = {
        title: id("currMovieTitle").value,
        movierating: id("currMovieRating").value,
        description: id("currMovieDescription").value,
        actor1: id("currActor1").value,
        actor2: id("currActor2").value,
        actor3: id("currActor3").value,
        image: id("currImage").value
    }

    console.log("test1");

    fetch("/createmovie", {
        method: "POST",
        body: JSON.stringify(newMovieStats),
        headers: { "Content-Type": "application/json" }
    }).then((res) => {console.log(res)})
    .catch((err) => {console.log(err)});
    // display it on the website somehow
}

/**
 * Gets all the movies and their info and adds each one as a div to
 */
function displayAllMovies() {
    fetch("/displayallmovies")
    .then((res) => {
        console.log(res);
        console.log(res.length);
        // create element for each movie
        for (let i = 0; i < res.length; i++) {
            let currDiv = document.createElement("div");
            let currTitle = document.createElement("p");
            currTitle.textContent = res[i].title;
            let currMovieRating = document.createElement("p");
            currMovieRating.textContent = res[i].movierating;
            let currImage = document.createElement("img");
            currImage.src = res[i].image;
            currImage.length = "400px";
            currImage.width = "300px";

            let allActors = "";
            if (res[i].actors[0] != null) {
                allActors += res[i].actors[0];
            }
            if (res[i].actors[1] != null) {
                allActors += ", " + res[i].actors[1];
            }
            if (res[i].actors[2] != null) {
                allActors += ", " + res[i].actors[2];
            }
            let currActors = document.createElement("p");
            currActors.textContent = allActors;


            currDiv.appendChild(currImage);
            currDiv.appendChild(currTitle);
            currDiv.appendChild(currMovieRating);
            currDiv.appendChild(currActors);

            /**
             * adds an event listener so that when any movie div is clicked
             * it will call a function which takes them to the individual
             * movie page and fills it with the correct info
             */
            currDiv.addEventListener("click", () => {
                let currTitle = res[i].title;
                generateIndividualMoviePage(currTitle);
            });

            id("allmovies").appendChild(currDiv);
            console.log("complete");
        }
    });
}

/**
 * Gets a JSON object containing all of the movie stats and adds them to the
 * individual movie page
 * @param {String} title
 */
function generateIndividualMoviePage(currTitle) {
    id("currenttitle").textContent = "";
    id("currentrating").textContent = "";
    id("currentactors").textContent = "";
    id("currentuserrating").textContent = "";
    id("currentuserreviews").textContent = "";

    let movieTitle = {
        title: currTitle
    }

    fetch("/generatepage", {
        method: "POST",
        body: JSON.stringify(movieTitle),
        headers: { "Content-Type": "application/json" }
    }).then((res) => {
        console.log(res);
        // update page stuff

        let allActors = "";
        if (res[0].actors[0] != null) {
            allActors += res[i].actors[0];
        }
        if (res[0].actors[1] != null) {
            allActors += ", " + res[i].actors[1];
        }
        if (res[0].actors[2] != null) {
            allActors += ", " + res[i].actors[2];
        }

        id("currenttitle").textContent = res[0].title;
        id("currentrating").textContent = res[0].movierating;
        id("currentactors").textContent = allActors;
    })
    .catch((err) => {console.log(err)});

    fetch("/generatepage2", {
        method: "POST",
        body: JSON.stringify(movieTitle),
        headers: { "Content-Type": "application/json" }
    }).then((res) => {
        id("currentuserrating").textContent = res[0].audiencerating;

        let numReviews = res[0].reviews.length;
        for (let i = 0; i < numReviews; i++) {
            let currReview = res[0].reviews[numReviews - i];
            let currDiv = document.createElement("div");
            currDiv.id = "review";
            currDiv.textContent = currReview;
            id("currentreviews").appendLast(currDiv);
        }
    }).catch((err) => {console.log(err)});
}


/**
 * When a user submits a rating, adds it to the rating database
 */
function addRating() {
    let addedRating = id("addedrating").value;
    if (adddedRating == "0" || addedRating == "1"
        || addedRating == "2" || addedRating == "3"
        || addedRating == "4" || addedRating == "5") {

        id("ratingdiv").textContent = "Thank you! Refresh the page to see the new rating!"

        let movieStats = {
            title: id("currenttitle").value,
            rating: addedRating
        }

        fetch("/addrating", {
            method: "POST",
            body: JSON.stringify(movieStats),
            headers: { "Content-Type": "application/json" }
        .then((res) => {console.log(res)})
        .catch(console.err)});
    } else {
        id("addedrating").textContent = "";
        alert("The rating must follow the given parameters");
    }
}

/**
 * When the user submits a review, adds it to the given movies reviews
 */
function addReview() {
    let addedReview = id("addedreview").value;
    id("reviewdiv").textContent = "Thank you! Refresh the page to see your review!";

    let movieStats = {
        title: id("currenttitle").value,
        review: addedReview
    }

    fetch("/addreview", {
        method: "POST",
        body: JSON.stringify(movieStats),
        headers: { "Content-Type": "application/json" }
    }).then((res) => {console.log(res)})
    .catch(console.log(err));
}


function id(name) {
    return document.getElementById(name);
}
