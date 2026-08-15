import { api } from "./api"

export async function loginRequest(data) {
  const response = await api.post("/api/auth/login", data)
  return response.data
}

export async function logoutRequest() {
  const response = await api.post("/api/auth/logout");
  return response.data
}

export async function registerRequest(data) {
  const response = await api.post("/users/register", data)
  return response.data
}

export async function getMeRequest() {
  const response = await api.get("/users/me", { withCredentials: true })
  return response.data
}

export async function deleteUserRequest() {
 const response = await api.delete("/users/me")
 return response.data
  
}