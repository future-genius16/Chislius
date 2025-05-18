import {createContext, useContext, useState} from 'react'

const TokenContext = createContext()

export const TokenProvider = ({children}) => {
    const [token, setToken] = useState(null)

    const login = (newToken) => {
        setToken(newToken)
        localStorage.setItem('token', newToken)
    }

    const logout = () => {
        setToken(null)
        localStorage.removeItem('token')
    }

    return (<TokenContext.Provider value={{token, login, logout}}>
        {children}
    </TokenContext.Provider>)
}

export const useAuth = () => useContext(TokenContext)