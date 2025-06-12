import {Fragment} from 'react'
import AuthScreen from './components/screen/AuthScreen'
import {useAuth} from './context/TokenContext'
import MainScreen from './components/screen/MainScreen'
import './App.scss'
import {ToastProvider} from './context/ToastProvider'

function App() {
    const {token} = useAuth()

    const renderScreen = () => {
        if (!token) {
            return <AuthScreen/>
        } else {
            return <MainScreen/>
        }
    }

    return (<>
        <ToastProvider>
            {renderScreen()}
        </ToastProvider></>)
}

export default App