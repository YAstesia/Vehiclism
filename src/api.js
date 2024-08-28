// src/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api', // 替换成你的 Spring Boot 后端地址
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
