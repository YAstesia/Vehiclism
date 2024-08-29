// src/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8081', //或者ip
  headers: {
    'Content-Type': 'application/json',
  },
});

export const login = (email, password) => {
  return api.post('/login', {
    email,
    password,
  });
};
