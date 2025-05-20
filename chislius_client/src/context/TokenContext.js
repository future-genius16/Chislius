import {createContext, useContext, useState} from 'react'
import api from "../client/ApiClient"

const TokenContext = createContext()

export const TokenProvider = ({children}) => {
    const [token, setToken] = useState(localStorage.getItem('token'))

    const login = (newToken) => {
        setToken(newToken)
        api.setAuthToken(newToken)
        localStorage.setItem('token', newToken)
    }

    const logout = () => {
        setToken(null)
        api.clearAuthToken()
        localStorage.removeItem('token')
    }

    return (<TokenContext.Provider value={{token, login, logout}}>
        {children}
    </TokenContext.Provider>)
}

export const useAuth = () => useContext(TokenContext)