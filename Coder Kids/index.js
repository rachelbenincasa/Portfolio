/**
 * Rachel Benincasa & Elizabeth Deng
 * 6 June 2023 2023
 *
 * The JS implementation for our computer science education company frontend.
 * Includes button actions on main page to guide the user to what they are looking for. Makes
 * requests to app.js in order to access the database for information about classes and users.
 */

"use strict";

(function() {
  let currUserId;
  let currUsername;
  let currLoggedIn;

  window.addEventListener("load", init);

  /**
   * Initializes button properties when the website first loads.
   */
  function init() {
    checkLogin();
    hidePgs();
    nav();
    id("show-create-button").addEventListener("click", () => {
      id("create-account-page").classList.toggle("hidden");
    });
    addListeners();
  }

  /**
   * Checks the browser data to determine whether the user is logged in.
   * Changes the view of the webpage based on whether a user is logged in.
   */
  function checkLogin() {
    if (window.sessionStorage.getItem("loggedin") === "true") {// use still logged in
      currLoggedIn = true;
      id("logout").classList.remove("hidden");
      id("viewcart").classList.remove("hidden");
      id("login").classList.add("hidden");
      let user = window.sessionStorage.getItem("username");
      currUserId = window.sessionStorage.getItem("userid");
      id("currentuser").textContent = "Login Status: Logged in as user: " + user;
      viewCart2(currUserId);
    } else {
      currLoggedIn = false;
      id("logout").classList.add("hidden");
      id("viewcart").classList.add("hidden");
      id("login").classList.remove("hidden");
    }
  }

  /**
   * Hides all pages upon initm except for the home page.
   */
  function hidePgs() {
    let pages = qsa(".page");
    for (let i = 0; i < pages.length; i++) {
      pages[i].classList.add("hidden");
    }
    id("home-page").classList.remove("hidden");
    id("courses-page").classList.add("hidden");
    id("cart-page").classList.add("hidden");
    id("filters").classList.add("hidden");
    id("srchbar").classList.add("hidden");
  }

  /**
   * Adds functionality to each of the navigation tabs.
   */
  function nav() {
    let navB = qsa(".navlink");
    for (let i = 0; i < navB.length; i++) {
      navB[i].addEventListener("click", (e) => {
        showPage(e);
      });
    }
  }

  /**
   * Adds event listeners to various buttons and other elements throughout the
   * HTML file. Also adds the hidden class to pages not currently being displayed.
   */
  function addListeners() {
    id("courses").addEventListener("click", displayAllCourses);
    id("submitinfo").addEventListener("click", getEmailInfo);
    id("create-new-account").addEventListener("click", createNewUser);
    id("loginbutton").addEventListener("click", loginuser);
    id("viewcart").addEventListener("click", showCart);
    id("logout").addEventListener("click", logoutuser);
    id("check-out-button").addEventListener("click", purchaseCourses);
    id("home").addEventListener("click", backtoMain);
    id("filterbtn").addEventListener("click", filterClasses);
    id("login").addEventListener("click", hideCourses);
    id("about").addEventListener("click", hideCourses);
    id("srchbtn").addEventListener("click", search);
  }

  /**
   * Hides the courses page from view when user navigates to a different page.
   */
  function hideCourses() {
    id("offer").classList.add("hidden");
    id("courses-page").classList.add("hidden");
    id("srchbar").classList.add("hidden");
    id("filters").classList.add("hidden");
    id("coursecards").innerHTML = "";
  }

  /**
   * Makes changes to the session storage and changes the user's status from
   * logged in to logged out. Hides the cart and login elements.
   */
  function logoutuser() {
    window.sessionStorage.setItem("loggedin", "false");
    id("viewcart").classList.add("hidden");
    id("logout").classList.add("hidden");
    id("login").classList.remove("hidden");
    id("currentuser").textContent = "Login Status: You are not currently logged in.";
    currUserId = null;
  }

  /**
   * Returns the user to the main screen when the main button is clicked. Hides
   * other pages.
   */
  function backtoMain() {
    id("login-page").classList.remove("hidden");
    id("coursecards").innerHTML = "";
    qs("main").classList.remove("hidden");
    id("filters").classList.add("hidden");
    id("srchbar").classList.add("hidden");
  }

  /**
   * Displays the cart page and retrieves the user's cart items.
   */
  function showCart() {
    id("indcourse-page").innerHTML = "";
    id("courses-page").classList.add("hidden");
    id("home-page").classList.add("hidden");
    id("about-page").classList.add("hidden");
    id("create-account-page").classList.add("hidden");
    id("cart-page").classList.remove("hidden");
    viewCart2();
  }

  /**
   * Makes a FETCH call to the /viewcart endpoint to retrieve data about the courses currently
   * stored in the user's cart.
   */
  function viewCart2() {
    fetch("/viewcart/" + currUserId)
      .then(statusCheck)
      .then(res => res.json())
      .then(displayCart)
      .catch(console.error);
  }

  /**
   * Displays the user's car t items on the cart page. Creates an article element
   * for each item inside the cart.
   * @param {Object} data - response data from fetch call to /viewcart endpoint
   */
  function displayCart(data) {
    id("courses-page").classList.add("hidden");
    for (let i = 0; i < data.length; i++) {
      let currCourse = data[i];
      let currCourseArticle = document.createElement("article");
      let coursename = document.createElement("p");
      coursename.textContent = currCourse.name;
      let coursetime = document.createElement("p");
      coursetime.textContent = "Date/Time: " + currCourse.date_time;
      let coursecost = document.createElement("p");
      coursecost.textContent = "Cost: " + currCourse.tuition;
      if (currCourse.purchased === "true") {
        currCourseArticle.append(coursename, coursetime, coursecost);
        id("purchased").appendChild(currCourseArticle);
      } else {
        let removecourse = document.createElement("button");
        removecourse.textContent = "Remove From Cart";
        removecourse.addEventListener("click", removeCurrCourse);
        let purchase = document.createElement("button");
        purchase.textContent = "Purchase";
        currCourseArticle.append(coursename, coursetime, coursecost);
        currCourseArticle.append(purchase, removecourse);
        purchase.addEventListener("click", () => {
          purchaseCourse(currCourse.course_id, currCourseArticle);
        });
        purchase.addEventListener("click", updateSeats(data[0].class_id));
        id("cart").appendChild(currCourseArticle);
      }
    }
  }

  /**
   * Makes a FETCH call to the /updateseats endpoint in order to update
   * the number of seats left in the class once a user has registered.
   * @param {Integer} data - course id
   */
  function updateSeats(data) {
    fetch("/updateseats/" + data)
      .then(statusCheck)
      .then(res => res.text)
      .catch(console.error);
  }

  /**
   * Adds the purchased course to the "purchased" section and removes it from the user's cart.
   * Makes a POST call to update the database regarding the course that was purchased,
   * adding it to the user's list of purchased courses.
   * @param {Object} courseid - numerical course id
   * @param {Object} currCourseArticle - DOM object
   */
  function purchaseCourse(courseid, currCourseArticle) {
    id("purchased").appendChild(currCourseArticle);
    if (id("cart").contains(currCourseArticle)) {
      id("cart").removeChild(currCourseArticle);
    }
    let purchaseData = new FormData();
    purchaseData.append("userid", currUserId);
    purchaseData.append("courseid", courseid);
    fetch("/purchaseindividual", {method: "POST", body: purchaseData})
      .then(statusCheck)
      .then(res => res.text)
      .catch(console.error);
  }

  /**
   * Removes a course from the user's shopping cart. Makes a POST call to the
   * /removecourse endpoint to remove the selected course from the user's row
   * in the database.
   */
  function removeCurrCourse() {
    let course = this.parentNode;
    let removeData = new FormData();
    removeData.append("userid", currUserId);
    removeData.append("courseid", course.children[1].textContent.split(":")[1].trim());
    course.innerHTML = "";
    fetch("/removecourse", {method: "POST", body: removeData})
      .then(statusCheck)
      .then(res => res.text())
      .catch(console.error);
  }

  /**
   * Displays the page connected to the navigation menu item that was clicked.
   * Hides all other pages from display.
   * @param {Event} e current event target
   */
  function showPage(e) {
    let pageId = e.currentTarget.id + "-page";
    let pages = qsa(".page");
    for (let i = 0; i < pages.length; i++) {
      if (pages[i].id !== pageId) {
        pages[i].classList.add("hidden");
      } else {
        pages[i].classList.remove("hidden");
      }
    }
  }

  /**
   * Logs the user in using the text within the input elements.
   * Makes a POST call to the /login endpoint to access the user's account data
   * and make changes to the browser status.
   */
  function loginuser() {
    id("login-page").classList.remove("hidden");
    let loginData = new FormData();
    loginData.append("username", id("username").value);
    loginData.append("password", id("password").value);
    fetch("/login", {method: "POST", body: loginData})
      .then(statusCheck)
      .then(res => res.text())
      .then(changeLoginStatus)
      .then(updateBrowserLoggedIn)
      .catch(displayloginerror);
  }

  /**
   * Temporarily displays an error message if there is an issue with logging
   * the user in.
   * @param {Object} data - The error message content
   */
  function displayloginerror(data) {
    let errormsg = document.createElement("p");
    errormsg.textContent = data;
    id("login-page").prepend(errormsg);
    setTimeout(() => {
      id("login-page").removeChild(errormsg);
      id("username").textContent = "";
      id("password").textContent = "";
    }, 3000);
  }

  /**
   * Changes the login status by updating the session storage and displaying a welcome message.
   * @param {Text} data - response data from the POST call
   */
  function changeLoginStatus(data) {
    currUsername = data.split(" ")[1];
    currLoggedIn = true;
    let welcomemessage = document.createElement("p");
    welcomemessage.textContent = data + "!";
    currUserId = welcomemessage.textContent.split(":")[1].split(")")[0];
    id("login-page").prepend(welcomemessage);
    id("currentuser").textContent = "Login Status: Logged in as user: " + currUsername;
    clearLoginTextBoxes();
    displayAllCartObjs();
    if (qs("#keeplogin").checked) {
      window.sessionStorage.setItem("loggedin", "true");
      window.sessionStorage.setItem("username", currUsername);
      window.sessionStorage.setItem("userid", currUserId);
    }
    setTimeout(() => {
      id("login-page").removeChild(welcomemessage);
      id("create-account-page").classList.add("hidden");
      id("home-page").classList.remove("hidden");
    }, 3000);
  }

  /**
   * Makes a GET call to the cart endpoint to display all objects within the user's cart.
   */
  function displayAllCartObjs() {
    fetch("/viewcart/" + currUserId)
      .then(statusCheck)
      .then(res => res.json())
      .then(displayCart)
      .catch(console.error);
  }

  /**
   * Clears the text from the elements related to logging in.
   */
  function clearLoginTextBoxes() {
    id("createfullname").value = "";
    id("createusername").value = "";
    id("createpassword").value = "";
    id("confirmpassword").value = "";
    id("username").value = "";
    id("password").value = "";
  }

  /**
   * Toggles page view when user is logged in.
   */
  function updateBrowserLoggedIn() {
    id("login").classList.add("hidden");
    id("logout").classList.remove("hidden");
    id("viewcart").classList.remove("hidden");
  }

  /**
   * Displays information about all courses offered on the "courses offered page"
   */
  function displayAllCourses() {
    id("offer").classList.remove("hidden");
    id("srchbar").classList.remove("hidden");
    id("filters").classList.remove("hidden");
    id("coursecards").classList.remove("hidden");
    fetch("/displayall")
      .then(statusCheck)
      .then(res => res.json())
      .then(createCourseObjects)
      .catch(console.error);
  }

  /**
   * Makes a call to the search endpoint to filter course objects based on
   * the terms entered into the search bar. Searches for matches in course name,
   * description, and tags.
   */
  function search() {
    let term = id("search").value;
    fetch("all?search=" + term)
      .then(statusCheck)
      .then(res => res.json())
      .then(createFiltered)
      .catch(console.error);
  }

  /**
   * Filteres course objects based on the filters selected by the user. If no
   * filters are chosen, displays all available courses.
   */
  function filterClasses() {
    let age = id("age-filter").value;
    let lvl = id("school-level-filter").value;
    let day = id("day-filter").value;
    let filters = new FormData();
    if (age.trim() !== "") {
      filters.append("age", age);
    }
    if (lvl !== "none" && lvl.trim() !== "") {
      filters.append("lvl", lvl);
    }
    if (day !== "none" && day.trim() !== "") {
      filters.append("day", day);
    }

    if (filters.length === 0) {
      fetch("/displayall")
        .then(statusCheck)
        .then(res => res.json())
        .then(createCourseObjects)
        .catch(console.error);
    } else {
      fetch("/filterall", {method: "POST", body: filters})
        .then(statusCheck)
        .then(res => res.json())
        .then(createFiltered)
        .catch(console.error);
    }
  }

  /**
   * Creates course objects based on the response from the
   * FETCH call to the endpoint for all current courses, and adds them to the courses page.
   * @param {Object} res - The response object containing the course data.
   */
  function createCourseObjects(res) {
    let courses = res.allcourses;
    for (let i = 0; i < courses.length; i++) {
      createChildNode(courses[i]);
    }
  }

  /**
   * Creates course cards for courses that match the filter criteria
   * @param {JSON} res course data
   */
  function createFiltered(res) {
    id("coursecards").innerHTML = "";
    if (res.length === 0) {
      timeMsg("no-match");
    } else {
      for (let i = 0; i < res.length; i++) {
        createChildNode(res[i]);
      }
    }
    id("search").value = "";
  }

  /**
   * Temporarily displays a message. Message text content depends on which element
   * id is passed into the function,
   * @param {String} displaymsg - id of text element to be displayed
   */
  function timeMsg(displaymsg) {
    let msg = id(displaymsg);
    setTimeout(() => {
      msg.classList.remove("hidden");
    }, 100);

    setTimeout(() => {
      msg.classList.add("hidden");
    }, 3500);
  }

  /**
   * Creates a new course card using the information received from a FETCH call.
   * @param {Object} obj - The course object containing the course details.
   */
  function createChildNode(obj) {
    let card = document.createElement("article");
    card.id = obj.shortname;
    card.classList.add("course-card");
    let name = document.createElement("h3");
    name.textContent = obj.name;
    name.classList.add("bold");
    let agerange = document.createElement("p");
    agerange.textContent = "Ages " + obj.age_lower + " to " + obj.age_upper;
    let available = document.createElement("p");
    available.textContent = "Seats Available: " + obj.seats_left + "/" + obj.size;
    let moreinfo = document.createElement("button");
    moreinfo.classList.add("classbtn");
    moreinfo.id = obj.shortname;
    let date = document.createElement("p");
    date.textContent = obj.date_time;
    let timecommitment = document.createElement("p");
    timecommitment.textContent = "Avg. Work Per Week: " + obj.hours + " hours";
    let cost = document.createElement("p");
    cost.textContent = "Cost: $" + obj.tuition;
    moreinfo.textContent = "Learn More";
    moreinfo.addEventListener("click", showMoreInfo);
    date.classList.add("hidden");
    timecommitment.classList.add("hidden");
    cost.classList.add("hidden");
    card.append(name, agerange, available, moreinfo, date, timecommitment, cost);
    id("coursecards").appendChild(card);
  }

  /**
   * Displays additional information about the course that was selected.
   * If user is logged in, adds the ability to enroll in the course. If the user
   * is not logged in, displays a message prompting them to log in.
   */
  function showMoreInfo() {
    let currCard = this.parentNode;
    let course = currCard.id;
    fetch("/course/" + course)
      .then(statusCheck)
      .then(res => res.json())
      .then(createCoursePg)
      .catch(console.error);

    hideCourses();
    id("indcourse-page").classList.remove("hidden");
  }

  /**
   * Creates and populates a separate page for an individual course. Includes
   * more detailed information about the course.
   * @param {JSON} res - information about the course
   */
  function createCoursePg(res) {
    res = res[0];
    let page = id("indcourse-page");
    let section1 = gen("div");
    let section2 = gen("div");
    let section3 = createSection3(res);
    section2.id = "course-head";
    section1.classList.add("details");
    let classNm = gen("h2");
    classNm.textContent = res.name;
    let ageGrp = gen("h3");
    ageGrp.textContent = "Ages: " + res.age_lower + " to " + res.age_upper;
    let time = gen("p");
    time.textContent = res.date_time;
    let back = gen("button");
    back.textContent = "Back to Courses";
    back.addEventListener("click", goBackToCourses);
    back.id = "back-to";
    let desc = gen("p");
    desc.textContent = res.desc;
    section2.append(classNm, ageGrp, time, desc, section3);
    section1.append(back, section2);
    page.appendChild(section1);
  }

  /**
   * Generates and returns a new HTML element with more information about a course.
   * @param {JSON} res - information about the course
   * @returns {Object} a new DOM element
   */
  function createSection3(res) {
    let section3 = gen("div");
    let seats = gen("h3");
    seats.textContent = "Seats Available: " + res.seats_left + "/" + res.size;
    let enroll = gen("button");
    enroll.textContent = "Enroll";
    enroll.id = "enroll-btn";
    enroll.addEventListener("click", () => {
      enrollIn(res);
    });
    let tuition = gen("h3");
    tuition.textContent = "Tuition: $" + res.tuition;
    section3.append(seats, tuition, enroll);
    return section3;
  }

  /**
   * Re-displays the page containing all courses.
   */
  function goBackToCourses() {
    id("indcourse-page").innerHTML = "";
    id("indcourse-page").classList.add("hidden");
    id("courses-page").classList.remove("hidden");
    displayAllCourses();
  }

  /**
   * If a user is currently logged in, adds the selected course to the cart. If they are not,
   * temporarily displays a message indicating the need to log in to register.
   * @param {OBject} res - metadata about a course
   */
  function enrollIn(res) {
    if (currLoggedIn) {
      addToCart(res);
    } else {
      let loginmessage = document.createElement("p");
      id("indcourse-page").appendChild(loginmessage);
      loginmessage.textContent = "Not currently logged in. Please log in to enroll";
      loginmessage.classList.add("hidden");
      loginmessage.id = "not-logged";
      timeMsg("not-logged");
    }
  }

  /**
   * Adds the selected course to the user's cart. Updates the database
   * to reflect that the user has added the course to their cart.
   * @param {JSON} res - information about the course
   */
  function addToCart(res) {
    let addedmsg = document.createElement("p");
    addedmsg.classList.add("bold");
    addedmsg.textContent = "Added to Cart!";
    let userData = new FormData();
    userData.append("id", currUserId);
    userData.append("coursename", res.name);
    userData.append("purchased", "false");
    fetch("/addcourse", {method: "POST", body: userData})
      .then(statusCheck)
      .then(resp => resp.text)
      .catch(console.error);
  }

  /**
   * Creates a new user and adds them to the database using the values
   * given in the text boxes.
   */
  function createNewUser() {
    if (id("createpassword").value === id("confirmpassword").value) {
      let newUserData = new FormData();
      newUserData.append("username", id("createusername").value);
      newUserData.append("password", id("createpassword").value);
      newUserData.append("fullname", id("createfullname").value);
      fetch("/createaccount", {method: "POST", body: newUserData})
        .then(statusCheck)
        .then(res => res.text)
        .then(showMessage)
        .catch(console.error);
    } else {
      let error = document.createElement("p");
      error.textContent = "Passwords do not match. Please re-enter and try again";
      id("create-account-page").append(error);
      id("createpassword").textContent = "";
      id("confirmpassword").textContent = "";
      setTimeout(() => {
        error.classList.add("hidden");
      }, 5000);
      id("createpassword").textContent = "";
      id("confirmpassword").textContent = "";
    }
  }

  /**
   * Edits the database when a user purchases a course.
   */
  function purchaseCourses() {
    let purchaseData = new FormData();
    purchaseData.append("userid", currUserId);
    fetch("/getpurchasestatus", {method: "POST", body: purchaseData})
      .then(statusCheck)
      .then(res => res.json())
      .then(updateCart)
      .catch(console.error);
  }

  /**
   * Only updates information for courses that were purchased by the user.
   * @param {Object} data - information about user's courses
   */
  function updateCart(data) {
    id("cart").innerHTML = "";
    for (let i = 0; i < data.usercourses.length; i++) {
      let currarticle = getCartElementData(data.usercourses[i]);
      id("purchased").appendChild(currarticle);
    }
    id("home-page").classList.remove("hidden");
    id("cart-page").classList.add("hidden");
  }

  /**
   * Creates a new DOM element with information about the specified course.
   * @param {Object} currCourse - metadata about the course
   * @returns {Object} a new article with the course information
   */
  function getCartElementData(currCourse) {
    let article = document.createElement("article");
    let name = document.createElement("p");
    name.textContent = currCourse.name;
    let id = document.createElement("p");
    id.textContent = "Course ID: " + currCourse.class_id;
    let date = document.createElement("p");
    date.textContent = "Date/Time: " + currCourse.date_time;
    let cost = document.createElement("p");
    cost.textContent = "Cost: $" + currCourse.tuition;
    article.append(name, id, date, cost);
    return article;
  }

  /**
   * Gets the information for a user's email from the text boxes, then makes a
   * POST request to update the email list in the database.
   */
  function getEmailInfo() {
    id("useremail").textContent = "";
    id("fullname").textContent = "";
    let data = new FormData();
    data.append("fullname", id("fullname").value);
    data.append("email", id("useremail").value);
    fetch("/addemaillist", {method: "POST", body: data})
      .then(statusCheck)
      .then(res => res.text)
      .then(showMessage)
      .catch(console.error);
  }

  /**
   * Shows a message for 5 seconds to user when user successfully enters their email.
   */
  function showMessage() {
    let p = document.createElement("p");
    p.textContent = "Success! You are now able to log in.";
    p.classList.add("bold");
    id("login-page").prepend(p);
    id("create-account-page").classList.add("hidden");
    clearLoginTextBoxes();
    p.classList.remove("hidden");
    setTimeout(() => {
      p.classList.add("hidden");
    }, 5000);
  }

  /**
   * check if an error occurs, if it does
   * throw an error
   * @param {Object} res - the response onject
   * @returns {Object} res - the response object
   */
  async function statusCheck(res) {
    if (!res.ok) {
      throw new Error(await res.text());
    }
    return res;
  }

  //  --------------------HELPER FUNCTIONS-------------------------------------

  /**
   * Returns a new DOM element with the specified tag type.
   * @param {string} tagName - object type
   * @returns {object} a new empty DOM element
   */
  function gen(tagName) {
    return document.createElement(tagName);
  }

  /**
   * Returns an object that holds the specific ID from the HTML file.
   * @param {string} name - HTML Object with ID
   * @returns {object} - The value that is held by the ID the user wants.
   */
  function id(name) {
    return document.getElementById(name);
  }

  /**
   * Returns the first element that matches the given CSS selector.
   * @param {string} query - CSS query selector.
   * @returns {object[]} array of DOM objects matching the query.
   */
  function qs(query) {
    return document.querySelector(query);
  }

  /**
   * Returns an array of elements matching the given query.
   * @param {string} query - CSS query selector.
   * @returns {array} - Array of DOM objects matching the given query.
   */
  function qsa(query) {
    return document.querySelectorAll(query);
  }
})();
