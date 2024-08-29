// src/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://192.168.43.129:8081', //或者ip
  headers: {
    'Content-Type': 'application/json',
  },
});

export const login = (key, pwd) => {
  return api.post('/user/login', {
    key,
    pwd,
  });
};
