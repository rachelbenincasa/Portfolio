# CoderKids API Documentation
This API provides information about the various courses offered in our
kids coding program.

## Get a List of all Courses Offered*
**Request Format:** /all/:search?

**Request Type:** GET

**Returned Data Format**: JSON

**Description:** Returns a list of all available classes or classes that match the optional
search query

**Example Request:** /all
**Example Response:**
```json
{
  "allcourses": [
    {
      "class_id": 4,
      "name": "Introduction to Python",
      "shortname": "python1",
      "tags": "python, intro, elementary, first",
      "desc": "Python for beginners with little to no programming experience. Covers fundamental syntax, data types (strings, numbers, lists, etc.), operators, control structures (if statements, loops), and functions.",
      "age_lower": 9,
      "age_upper": 12,
      "school_lvl": "elementary",
      "size": 15,
      "seats_left": 15,
      "date_time": "Thursdays 4:30-5:30 PM",
      "hours": 10,
      "tuition": 480
    },
    {
      "class_id": 5,
      "name": "Introduction to Java",
      "shortname": "java1",
      "tags": "java, intro, elementary, first",
			... }
		... ]
		}
		// ... more JSON objects
```

**Example Request:** /all?search=java
**Example Response:**
```json
[
  {
    "class_id": 5,
    "name": "Introduction to Java",
    "shortname": "java1",
    "tags": "java, intro, elementary, first",
    "desc": "Java for beginners with little to no programming experience. Covers fundamental syntax, data types (strings, numbers, lists, etc.), operators, control structures (if statements, loops), and functions.",
    "age_lower": 9,
    "age_upper": 12,
    "school_lvl": "elementary",
    "size": 15,
    "seats_left": 15,
    "date_time": "Thursdays 3:30-4:30 PM",
    "hours": 10,
    "tuition": 480
  } //... more JSON objects
]
```
**Error Handling:**
Throws a 500 level error if a server issue occurs

## Filter classes based on optional parameters
**Request Format:** /all/filter

**Request Type:** POST

**Returned Data Format**: JSON

**Description:** Filters classes based on age, school level, and date.
Users can choose to use all 3 filter options, or only one.
Returns a json object containing all courses that meet the required filters.

**Example Request:** /all/filter
**Params:** [9, 9]

**Example Response:**
```json
[
  {
    "class_id": 4,
    "name": "Introduction to Python",
    "shortname": "python1",
    "tags": "python, intro, elementary, first",
    "desc": "Python for beginners with little to no programming experience. Covers fundamental syntax, data types (strings, numbers, lists, etc.), operators, control structures (if statements, loops), and functions.",
    "age_lower": 9,
    "age_upper": 12,
    "school_lvl": "elementary",
    "size": 15,
    "seats_left": 15,
    "date_time": "Thursdays 4:30-5:30 PM",
    "hours": 10,
    "tuition": 480
  }, //... more JSON objects
]
```
**Error Handling:** If there is a server error, a 500 level error is returned with
- "Server Error".


## Login User.
**Request Format:** /login endpoint with POST parameters of 'username' and 'password.

**Request Type:** POST

**Returned Data Format**: Plain Text

**Description:** Makes the current webpage tailored to the specific user, displays
a welcome message for the specific account

**Example Request:** /login with POST parameters "username=rachelbc" and
"password=password123"

**Example Response:**
```
Welcome "username" (User ID: #)
```

**Error Handling:**
- If no username matching the query is found, returns a 404 error and prompts the
- user to add their information to the user database by creating an account. If password
- does not match the password associated with the username, returns a 403 - Forbidden error
- due to the user entering the wrong credentials.

## Get Specific Course
**Request Format:** /course/:sn

**Request Type:** GET

**Returned Data Format:** JSON

**Description:** Returns one JSON object with the metadata about the course matching the shortname that was passed in.

**Example Request:** /course/java2

**Example Response:**
```json
[
  {
    "class_id": 7,
    "name": "Intermediate Java",
    "shortname": "java2",
    "tags": "java, medium, elementary, second",
    "desc": "Java for beginners with some previous Java experience. Covers more advanced functions and data structures.",
    "age_lower": 10,
    "age_upper": 12,
    "school_lvl": "elementary",
    "size": 15,
    "seats_left": 15,
    "date_time": "Tuesdays 4:30-5:30 PM",
    "hours": 10,
    "tuition": 480
  }
]
```
**Error Handling:**
If there is no course with a shortname that matches the parameter, throws a 400 error with the
message "No Course with that Shortname."
If there is a server error, send 500 error with message "Server Error. Please Try Again"

## Display All Courses
**Request Format:** /displayall

**Request Type:** GET

**Returned Data Format:** JSON

**Description:**  Displays all available data for all classes currently being offered.

**Example Request:** /displayall

**Example Response:**
```json
{
  "allcourses": [
    {
      "class_id": 4,
      "name": "Introduction to Python",
      "shortname": "python1",
      "tags": "python, intro, elementary, first",
      "desc": "Python for beginners with little to no programming experience. Covers fundamental syntax, data types (strings, numbers, lists, etc.), operators, control structures (if statements, loops), and functions.",
      "age_lower": 9,
      "age_upper": 12,
      "school_lvl": "elementary",
      "size": 15,
      "seats_left": 15,
      "date_time": "Thursdays 4:30-5:30 PM",
      "hours": 10,
      "tuition": 480
    },
    {
      "class_id": 5,
      "name": "Introduction to Java",
			//...
		} //... more JSON objects
	]
}
```
**Error Handling:**
If there is a server error, send 500 error with message "Server Error. Please Try Again"

## Filter Courses
**Request Format:** /filterall

**Request Type:** POST

**Returned Data Format:** JSON

**Description:**  Displays all available data for all classes that match the filter parameters.

**Example Request:** /filterall; POST parameters [elementary]

**Example Response:**
```json
[
  {
    "class_id": 4,
    "name": "Introduction to Python",
    "shortname": "python1",
    "tags": "python, intro, elementary, first",
    "desc": "Python for beginners with little to no programming experience. Covers fundamental syntax, data types (strings, numbers, lists, etc.), operators, control structures (if statements, loops), and functions.",
    "age_lower": 9,
    "age_upper": 12,
    "school_lvl": "elementary",
    "size": 15,
    "seats_left": 15,
    "date_time": "Thursdays 4:30-5:30 PM",
    "hours": 10,
    "tuition": 480
  },
  {
    "class_id": 5,
    "name": "Introduction to Java",
    "shortname": "java1",
    "tags": "java, intro, elementary, first",
    "desc": "Java for beginners with little to no programming experience. Covers fundamental syntax, data types (strings, numbers, lists, etc.), operators, control structures (if statements, loops), and functions.",
    "age_lower": 9,
    "age_upper": 12,
    "school_lvl": "elementary",
		//...
	} //... more JSON objects
]
```
**Error Handling:**
If there is a server error, send 500 error with message "Server Error. Please Try Again"

## Create User Account
**Request Format:** /createaccount

**Request Type:** POST

**Returned Data Format**: Plain text

**Description:** Using the parameters given (name, username, password), creates
the users account and adds it to the database.

**Example Request:** /createaccount

**Example Response:**

```
Account Created
```

**Error Handling:**
If one or more parameters is missed, throw 400 error with message "Missing One or More Parameters"
If there is a server error, send 500 error with message "Server Error. Please Try Again"

## Add to Email List
**Request Format:** /addemaillist

**Request Type:** POST

**Returned Data Format** Plain text

**Description:** Given a name and email address, adds them to the emaillist database for easy
access to send emails in the future.

**Example Request:** /addemaillist

**Example Response:**

```
Successfully added to email list
```

**Error Handling:**
If server error occurs, send a 500 level error with message "Server Error. Please Try Again"

## Display All Courses
**Request Format:** /displayall

**Request Type:** GET

**Returned Data Format** JSON

**Description:** Returns a JSON object containing all courses and their attributes.

**Example Request:** /displayall

**Example Response:**

```json
{
	{
		"name": "Intro Java",
		"age_lower": "9",
		"age_upper": "12",
		"class-size": 15
	}
	{
		"name": "Intro Python",
		"age_lower": "8",
		"age_upper": "11",
		"class-size": 10
	}
}
```

**Error Handling:**
If server error occurs, send a 500 level error with message "Server Error. Please Try Again"

## Add Course To Cart
**Request Format:** /addcourse

**Request Type:** POST

**Returned Data Format** Plain text

**Description:** Given a course if and a user id, adds to the database of courses in users carts
Responds with "complete" when done

**Example Request:** /addcourse

**Example Response:**

```
complete
```

**Error Handling:**
If one or more parameters is missed, throw 400 error with message "Missing One or More Parameters"
If there is a server error, send 500 error with message "Server Error. Please Try Again"


## Get Users Cart Information
**Request Format:** /viewcart/:userid

**Request Type:** GET

**Returned Data Format** JSON

**Description:** Returns a JSON object containing all of the courses and their attributes that
the current user has in their cart.

**Example Request:** /viewcart/1

**Example Response:**

```json
{
	"name": "Intro Java",
	"class_id": "12",
	"date_time": "Thursdays 7pm",
	"tuition": "500"
}
```

**Error Handling:**
If one or more parameters is missed, throw 400 error with message "Missing UserID parameter"
If there is a server error, send 500 error with message "Server Error. Please Try Again"

## Purchase Specific Users Cart
**Request Format:** /getpurchasestatus

**Request Type:** POST

**Returned Data Format** JSON

**Description:** Changes all of the unpurcahsed courses in the users course list to purchased.
Returns a JSON object containing all courses and their name, id, date, cost, and purchase status.

**Example Request:** /purchasecart

**Example Response:**

```json
{
	"name": "Intro Java",
	"class_id": "12",
	"date_time": "Thursdays 7pm",
	"tuition": "500",
	"purchased": "true"
}
```

**Error Handling:**
If ID parameter is not given, throw 400 error with message "Missing ID parameter"
If there is a server error, send 500 error with message "Server Error. Please Try Again"

## Remove Course from a specific users cart
**Request Format:** /removecourse

**Request Type:** POST

**Returned Data Format** Plain text

**Description:** Given a userid and courseid, deletes the course from the database and the users
cart. Sends "complete" when done

**Example Request:** /removecourse

**Example Response:**

```
complete
```

**Error Handling:**
If either parameter is not given, throw 400 error with message "Missing one or more parameters"
If there is a server error, send 500 error with message "Server Error. Please Try Again"

## Purchase Individual Course
**Request Format:** /purchaseindividual

**Request Type:** POST

**Returned Data Format** Plain text

**Description:** Given a userid and courseid, purchases the given course for the given user.
Returns a message saying "complete" when done.

**Example Request:** /purchaseindividual

**Example Response:**

```
complete
```

**Error Handling:**
If either parameter is not given, throw 400 error with message "Missing one or more parameters"
If there is a server error, send 500 error with message "Server Error. Please Try Again"

## Update Seats
**Request Format:** /updateseats/:course

**Request Type:** GET

**Returned Data Format** Plain text

**Description:** Given a course id, updates the course's state in the database to reflect the number
of seats left. Either decrements the number of available seats, available waitlist seats, or neither if
both no longer have seats available.

**Example Request:** /updateseats/23

**Example Response:**

```
You have successfully registered!
```

**Error Handling:**
If either parameter is not given, throw 400 error with message "Missing one or more parameters"
If there is a server error, send 500 error with message "Server Error. Please Try Again"

