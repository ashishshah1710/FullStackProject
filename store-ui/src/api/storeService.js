import axios from 'axios';
const BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8081';

export const createStore = (data) => axios.post(`${BASE_URL}/store/createStore`, data);
export const getStore = (id) => axios.get(`${BASE_URL}/store/${id}`);
export const updateStore = (data) => axios.put(`${BASE_URL}/store/updateStore`, data);
export const deleteStore = (id) => axios.delete(`${BASE_URL}/store/${id}`);

// Optional: Add a function to get all stores
export const getAllStores = () => axios.get(`${BASE_URL}/store/getAllStores`);
