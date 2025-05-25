// Import required modules
const express = require('express');
const mysql = require('mysql2');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const bodyParser = require('body-parser');
const cors = require('cors');

// Create an Express app
const app = express();
const port = 3000;

// Middleware
app.use(cors());
app.use(bodyParser.json());

// Connect to MySQL database
const db = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: 'happyparrot',
  database: 'auth_db', // Create this database in MySQL
});

// Connect to MySQL
db.connect((err) => {
  if (err) {
    console.error('Error connecting to MySQL:', err.message);
    return;
  }
  console.log('Connected to MySQL database.');
});

// Create Users table if it doesn't exist
db.query(
  `CREATE TABLE IF NOT EXISTS users (
      id INT AUTO_INCREMENT PRIMARY KEY,
      email VARCHAR(255) NOT NULL UNIQUE,
      password VARCHAR(255) NOT NULL
  )`,
  (err, result) => {
    if (err) throw err;
    console.log('Users table is set up.');
  }
);

// Registration API
app.post('/register', async (req, res) => {
  const { email, password } = req.body;
  
  // Log input data for debugging
  console.log("Registration Request Received - Email:", email, "Password:", password);

  // Check if user already exists
  db.query('SELECT * FROM users WHERE email = ?', [email], async (err, result) => {
    if (err) {
      console.error("Database error during user lookup:", err);
      return res.status(500).send('Database error.');
    }

    // Check if user already exists
    if (result.length > 0) {
      console.log("User already exists in the database:", email);
      return res.status(400).send('User already exists.');
    }

    // Hash the password
    const hashedPassword = await bcrypt.hash(password, 10);

    // Insert user into the database
    db.query(
      'INSERT INTO users (email, password) VALUES (?, ?)',
      [email, hashedPassword],
      (err, result) => {
        if (err) {
          console.error("Database error during user insertion:", err);
          return res.status(500).send('Error creating user.');
        }
        console.log("User registered successfully:", email);
        res.status(201).send('User registered successfully.');
      }
    );
  });
});


// Login API
app.post('/login', (req, res) => {
  const { email, password } = req.body;

  // Check for user in database
  db.query('SELECT * FROM users WHERE email = ?', [email], async (err, result) => {
    if (err) return res.status(500).send('Database error.');
    if (result.length === 0) return res.status(404).send('User not found.');

    const user = result[0];
    // Compare passwords
    const isPasswordValid = await bcrypt.compare(password, user.password);
    if (!isPasswordValid) return res.status(401).send('Invalid credentials.');

    // Generate JWT token
    const token = jwt.sign({ id: user.id }, 'peculiarpants', { expiresIn: '1h' });

    res.json({ message: 'Login successful', token });
  });
});

// Start the server
app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});