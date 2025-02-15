/**
 * Rachel Benincasa & Elizabeth Deng
 * 6 June 2023 2023
 *
 * The JS implementation for our computer science education company backend.
 * Includes endpoints to get metadata about courses, users, and login status.
 */

'use strict';

const express = require('express');
const app = express();

const sqlite = require('sqlite');
const sqlite3 = require('sqlite3');
const multer = require('multer');

app.use(express.urlencoded({extended: true}));
app.use(express.json());
app.use(multer().none());

/**
 * GET endpoint to return all available classes OR all classes matching an optional
 * search query. If no classes match the search query, an empty object is return.
 * Returns an array of JSON objects. Throws a 500 error if the server encounters a problem.
 */
app.get('/all/:search?', async (req, res) => {
  try {
    let qry = "SELECT * FROM classes LEFT JOIN classes2";
    qry += " ON classes.class_id = classes2.class_id";
    let search = qry += " WHERE classes.name LIKE ? OR classes.tags LIKE ? OR classes.desc LIKE ?";
    let term = "%" + req.query.search + "%";
    let db = await getDBConnection();
    if (!req.query.search) {
      let results = await db.all(qry);
      await db.close();
      res.json(results);
    } else {
      let results = await db.all(search, (term, term, term));
      await db.close();
     res.json(results);
    }
    } catch (err) {
    res.status(500).send("Server Error.");
  }
});

/**
 * POST endpoint for filtering classes based on age, school level, and date.
 * Uses POST body to filter through class data based on selected parameters.
 * If no classes are available, returns an empty object.
 * Throws 500 error if server runs into issues.
 */
app.post('/all/filter', async (req, res) => {
  try {
    let {age, schoolLvl, date} = req.body;
    let query = "SELECT * FROM classes LEFT JOIN classes2";
    query += " ON classes.class_id = classes2.class_id WHERE";
    let params = [];

    if (age) {
      query += ' age_lower <= ? AND age_upper >= ?';
      params.push(age);
      params.push(age);
    }

    if (schoolLvl) {
      query += ' AND school_lvl = ?';
      params.push(schoolLvl);
    }

    if (date) {
      query += ' AND date_time LIKE ?';
      let term = "%" + date + "%";
      params.push(term);
    }

    let db = await getDBConnection();
    let results = await db.all(query, params);
    await db.close();
    res.json(results);
  } catch (error) {
    res.status(500).send('Server Error.');
  }
});

/**
 * POST endpoint that logs in a specific user so that courses added to their account
 * will be stored in the database.
 * Takes in body parameters "username" and "password".
 * Sends a text response: "Welcome" if login successful.
 * Throws: 403 error if password does not match what is stored in the database, 404 error if
 * user does not exist, and 500 error if server runs into issues.
 */
app.post("/login", async function(req, res) {
  let db = await getDBConnection();
  let user = req.body.username;
  let pass = req.body.password;
  let userquery = "SELECT username FROM users WHERE username = ?";
  try {
    let enteredUsername = await db.all(userquery, user);
    if (enteredUsername.length !== 0) {
      let query = "SELECT password FROM users WHERE username = ?";
      let enteredPassword = await db.all(query, user);
      let query2 = "SELECT id FROM users WHERE username = ?";
      let userId = await db.all(query2, user);
      await db.close();
      if (enteredPassword[0].password === pass) {
        res.type("text")
          .send("Welcome " + user + " (User Id: " + userId[0].id + ")");
      } else {
        res.status(400).send("Password Incorrect. Please Try Again!");
      }
    } else {
      res.status(400).send("User Not Found. Check Your Spelling Or Create Account!");
    }
  } catch (err) {
    res.status(500).send("Server Error. Please Try Again!");
  }
});

/**
 * GET endpoint to access all available data for one course with the specific
 * shortname passed into the route parameter.
 * will be stored in the database.
 * Takes in route parameter "sn" (shortname.)
 * Sends a JSON response with metadata for one course.
 * Throws: 400 error if no course matches the given shortname.
 * Throws 500 error if server runs into issues.
 */
app.get("/course/:sn", async (req, res) => {
  let shortname = req.params.sn;
  let query = "SELECT * FROM classes LEFT JOIN classes2";
  query += " ON classes.class_id = classes2.class_id";
  query += " WHERE classes.shortname = ?";
  try {
    let db = await getDBConnection();
    let result = await db.all(query, [shortname]);
    await db.close();
    if (result.length > 0) {
      res.json(result);
    } else {
      res.type("text").status(400).send("No Course with that Shortname.");
    }
  } catch (err) {
    res.type("text").status(500).send("Server Error. Please Try Again!");
  }
});
/**
 * POST endpoint for users to create an account. Account is added to the users
 * database based on the information supplied in the body.
 * Throws: 400 error if body is missing one or more parameters, 500 error if server
 * encounters issues.
 */
app.post("/createaccount", async function(req, res) {
  try {
    let db = await getDBConnection();
    let user = req.body.username;
    let pass = req.body.password;
    let name = req.body.fullname;
    if (user && pass && name) {
      let query = "INSERT INTO users(username, password, fullname) VALUES (?, ?, ?)";
      await db.run(query, [user, pass, name]);
      await db.close();
      res.type("text").send("Account created.");
    } else {
      res.type("text").status(400).send("Missing One Or More Parameters.");
    }
  } catch (err) {
    res.type("text").status(500).send("Server Error. Please Try Again");
  }
});

/**
 * POST endpoint for users to add their email to the email list.
 * Inserts their full name and email into the email list database.
 * Throw 500 error if server encounters an issue.
 */
app.post("/addemaillist", async (req, res) => {
  try {
    let db = await getDBConnection();
    let user = req.body.user;
    let email = req.body.email;
    let query = "INSERT INTO emails(fullname, email) VALUES (?, ?)";
    await db.run(query, [user, email]);
    await db.close();
    res.type("text");
    res.send("Successfully added to mailing list.");
  } catch (err) {
   res.type("text").status(500).send("Server Error. Please Try Again");
  }
});

/**
 * GET endpoint to display all available data for all classes currently being offered.
 * Returns a JSON object containing an array of all courses and their metadata.
 * Throws a 500 error if the server encounters an error.
 */
app.get("/displayall", async (req, res) => {
  try {
    let db = await getDBConnection();
    let query = "SELECT * FROM classes LEFT JOIN classes2";
    query += " ON classes.class_id = classes2.class_id";
    let response = await db.all(query);
    await db.close();
    res.json({"allcourses": response});
  } catch (err) {
    res.type("text").status(500).send("Server Error. Please Try Again");
  }
});

/**
 * POST endpoint to display all available data for classes that match the filter
 * queries passed in through the request body. If no filters are passed in, returns
 * all available classes.
 * Throws a 500 error if server encounters an error.
 */
app.post("/filterall", async (req, res) => {
  try {
    let db = await getDBConnection();
    if (!req.body.age && !req.body.lvl && !req.body.day) {
      let qr = "SELECT * FROM classes LEFT JOIN classes2";
      qr += " ON classes.class_id = classes2.class_id";
      let resp = await db.all(qr);
      res.json(resp);
    } else {
      let qr = filterQry(req);
      let params = filterParams(req);
      let resp = await db.all(qr, params);
      res.json(resp);
    }
    await db.close();
  } catch (err) {
    res.type("text").status(500).send("Server Error. Please Try Again");
  }
});

/**
 * Adds the given name and email address to the correct spots in their database.
 * Sends a response saying "complete"
 * Throws a 500 error if the server encounters an error.
 */
app.post("/addemaillist", async function(req, res) {
  res.type("text");
  try {
    let db = await getDBConnection();
    let user = req.body.fullname;
    let email = req.body.email;
    let query = "INSERT INTO emaillist(fullname, email) VALUES (?, ?)"
    await db.run(query, [user, email]);
    await db.close();
    res.send("complete");
  } catch (err) {
    res.type("text").status(500).send("Server Error. Please Try Again");
  }
});

/**
 * Adds the given course and the user who added it to the database.
 * Sends a response saying "complete"
 * Throws a 500 error if the server encounters an error.
 */
app.post("/addcourse", async function(req, res) {
  let userid = req.body.id;
  let coursename = req.body.coursename;
  let status = req.body.purchased;
  let purchaseID = await getPurchaseId(userid);
  if (userid && coursename && status) {
    try {
      let db = await getDBConnection();
      let query1 = "SELECT class_id FROM classes WHERE name = ?";
      let classId = await db.all(query1, coursename);
      let query2 = "INSERT INTO cart(course_id, user_id, purchased, purchaseID) ";
      query2 += "VALUES (?, ?, ?, ?)";
      await db.all(query2, [classId[0].class_id, userid, status, purchaseID]);
      await db.close();
      res.type("text").send("complete");
    } catch (err) {
      res.status(500).send("Server Error. Please Try Again.")
    }
  } else {
    res.type("text").status(400).send("Missing One Or More Parameters");
  }
});

/**
 * Returns a JSON object containing the class id, name, date, and cost for each course in a given
 * users cart
 * Throws a 500 error if the server encounters an error.
 * Throws a 400 error if no parameter is given
 */
app.get("/viewcart/:userid", async function(req, res) {
  let userid = req.params.userid;
  if (req.params.userid) {
    try {
      let db = await getDBConnection();
      let query = "SELECT c1.class_id, c1.name, c2.date_time, c2.tuition, " +
      "ca.purchased FROM cart ca, classes c1,"
      + " classes2 c2 WHERE ca.course_id = c1.class_id AND ca.course_id = c2.class_id AND"
      + " user_id = ?";
      let currCourseIds = await db.all(query, userid);
      await db.close();
      res.json(currCourseIds);
    } catch (err) {
      res.status(500).send("Server Error. Please Try Again.");
    }
  } else {
    res.status(400).send("Missing UserID parameter");
  }
});

/**
 * Changes all items in a users cart to purchased if they were not already. Returns a JSON
 * object with the class id, name, date, cost, and the purchase status for each course.
 * Throws a 500 error if the server encounters an error.
 * Throws a 400 error if no parameter is given
 */
app.post("/getpurchasestatus", async function(req, res) {
  let id = req.body.userid;
  if (id) {
    try {
      let db = await getDBConnection();
      let query = "UPDATE cart SET purchased = 'true' WHERE purchased = 'false' AND user_id = ?";
      await db.all(query, id);
      let query2 = "SELECT c1.class_id, c1.name, c2.date_time, c2.tuition, ca.purchased FROM"
      + " cart ca, classes c1, classes2 c2 WHERE ca.course_id = c1.class_id AND"
      + " ca.course_id = c2.class_id AND user_id = ?";
      let upd8 = "UPDATE classes2 SET seats_left = seats_left - 1 WHERE cart.course_id = classes2.class_id";
      await db.run(upd8);
      let ans = await db.all(query2, id);
      await db.close();
      res.json({"usercourses": ans});
    } catch (err) {
      res.type("text").status(500).send("Server Error. Please Try Again");
    }
  } else {
    res.type("text").status(400).send("Missing ID parameter");
  }
});

/**
 * Removes a speficic users course from the database.
 * Returns a message saying "complete" if this is done successfully
 * Throws a 500 error if the server encounters an error.
 * Throws a 400 error if either parameter is not given
 */
app.post("/removecourse", async function(req, res) {
  let userid = req.body.userid;
  let courseid = req.body.courseid;
  if (req.body.userid && req.body.courseid) {
    try {
      let db = await getDBConnection();
      let query = "DELETE FROM cart WHERE user_id = ? AND course_id = ?";
      await db.all(query, [userid, courseid]);
      await db.close();
      res.type("text").send("complete");
    } catch (err) {
      res.type("text").status(500).send("Server Error. Please Try Again.");
    }
  } else {
    res.type("text").status(400).send("Missing at least one parameter.")
  }
});

/**
 * Purchases an individiual course for a specific user
 * Returns a message saying "complete" if this is done successfully
 * Throws a 500 error if the server encounters an error.
 * Throws a 400 error if either parameter is not given
 */
app.post("/purchaseindividual", async function(req, res) {
  let userid = req.body.userid;
  let courseid = req.body.courseid;
  if (userid && courseid) {
    try {
      let db = await getDBConnection();
      let query = "UPDATE cart SET purchased = 'true' WHERE purchased = 'false'";
      query += " AND user_id = ? AND course_id = ?";
      await db.all(query, [userid, courseid]);
      await db.close();
      res.type("text").send("complete");
    } catch (err) {
      res.status(500).send("Server Error. Please Try Again.");
    }
  } else {
    res.type("text").send("Missing one or more parameters")
  }
});

/**
 * Updates the database when a user successfully registers for a class, to reflect
 * the new number of seats available in that class. If there are no seats left,
 * adds the user to the waitlist. If there are no seats in the waitlist either, notifies
 * the user that their request was unsuccessful.
 */
app.get("/updateseats/:course", async (req, res) => {
  if (req.params.course) {
    try {
      let db = await getDBConnection();
      let qry1 = "SELECT seats_left, waitlist FROM classes2 WHERE class_id = ?";
      let res1 = await db.all(qry1, [req.params.course]);
      res1 = res1[0];
      let seats = res1.seats_left.toString();
      let wl = res1.waitlist.toString();
      if (seats === 0) {
        if (wl === 0) {
          res.type("text").send("This class and its waitlist are full.");
        } else {
          let wlqry = "UPDATE classes2 SET waitlist = waitlist - 1 WHERE class_id = ?";
          await db.run(wlqry, [req.params.course]);
          res.type("text").send("You have been added to the waitlist.");
        }
      } else {
        let seatsqry = "UPDATE classes2 SET seats_left = seats_left - 1 WHERE class_id = ?";
        await db.run(seatsqry, [req.params.course]);
        res.type("text").send("You have successfully registered!");
      }
    } catch (err) {
      res.type("text").status(500).send("Server Error. Please Try Again.");
    }
  } else {
    res.status(400).send("Missing required parameter.");
  }
});

/**
 * Creates an SQL query reflecting the parameters within the request object.
 * @param {Object} req - request information
 * @returns {String} An SQL query
 */
function filterQry(req) {
  let age = req.body.age;
  let lvl = req.body.lvl;
  let qr = "SELECT * FROM classes LEFT JOIN classes2";
  qr += " ON classes.class_id = classes2.class_id";
  if (age && lvl && req.body.day) {
    qr += " WHERE classes2.age_lower <= ? AND classes2.age_upper >= ?";
    qr += " AND classes2.school_lvl = ?";
    qr += " AND classes2.date_time LIKE ?";
  } else if (age && lvl) {
    qr += " WHERE classes2.age_lower <= ? AND classes2.age_upper >= ?";
    qr += " AND classes2.school_lvl = ?";
  } else if (age && req.body.day) {
    qr += " WHERE classes2.age_lower <= ? AND classes2.age_upper >= ?";
    qr += " AND classes2.date_time LIKE ?";
  } else if (lvl && req.body.day) {
    qr += " WHERE classes2.school_lvl = ? AND classes2.date_time LIKE ?";
  } else if (age) {
    qr += " WHERE classes2.age_lower <= ? AND classes2.age_upper >= ?";
  } else if (lvl) {
    qr += " WHERE classes2.school_lvl = ?";
  } else if (req.body.day) {
    qr += " WHERE classes2.date_time LIKE ?";
  }
  return qr;
}

/**
 * Creates an array of parameters to pass into a POST request.
 * @param {Object} req - request information
 * @returns {Array} An array of parameters
 */
function filterParams(req) {
  let age = req.body.age;
  let lvl = req.body.lvl;
  let day = "%" + req.body.day + "%";
  let params = [];

  if (age) {
    params.push(age);
    params.push(age);
  }
  if (lvl) {
    params.push(lvl);
  }
  if (req.body.day) {
    params.push(day);
  }
  return params;
}

/**
 * Establishes a database connection to the database and returns the database object.
 * Any errors that occur should be caught in the function that calls this one.
 * @returns {Object} - The database object for the connection.
 */
async function getDBConnection() {
  const db = await sqlite.open({
    filename: 'data.db',
    driver: sqlite3.Database
  });
  return db;
}

/**
 * Creates the unique purchase id for the course
 * @param {Integer} id - current user id
 * @returns {String} - purchase ID string
 */
async function getPurchaseId(id) {
  let numCodes = ["a", "b", "c", "d", "f", "g", "h", "i", "j", "k", "l"];
  let code = id + "";
  for (let i = 0; i < 6; i++) {
    code += numCodes[Math.floor(Math.random() * 11)];
  }
  return code;
}

app.use(express.static('public'));
const PORT = process.env.PORT || 8000;
app.listen(PORT);