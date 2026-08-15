import axios from "axios"

export const api = axios.create({
  baseURL: "http://192.168.1.4:3000",
  //baseURL :"https://e4ad-176-42-136-148.ngrok-free.app",
  withCredentials: true,
})