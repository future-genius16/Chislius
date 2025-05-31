import {Fragment} from 'react'
import AuthScreen from './components/screen/AuthScreen'
import {useAuth} from './context/TokenContext'
import MainScreen from './components/screen/MainScreen'
import "./App.scss"

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
        {renderScreen()}
    </>)
}

export default App