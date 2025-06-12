import React, {createContext, useCallback, useState} from 'react'
import ToastContainer from 'react-bootstrap/ToastContainer'
import Toast from 'react-bootstrap/Toast'
import Player from '../components/game/Player'

export const ToastContext = createContext()

export const ToastProvider = ({children}) => {
    const [toasts, setToasts] = useState([])

    const addToast = useCallback((data) => {
        const id = Date.now()
        let toast
        switch (data.type) {
            case 'JOIN':
                toast = {id: id, data: {text: 'Присоединяется к комнате', user: data.user}, options: {}}
                break
            case 'LEAVE':
                toast = {id: id, data: {text: 'Покидает комнату', user: data.user}, options: {}}
                break
            case 'SUCCESS':
                toast = {
                    id: id,
                    data: {text: 'Верный ход. Получено очков: ' + data.score, user: data.user},
                    options: {variant: 'success'}
                }
                break
            case 'FAIL':
                toast = {
                    id: id,
                    data: {text: 'Неправильный ход. Потеряно очков: ' + data.score, user: data.user},
                    options: {variant: 'danger'}
                }
                break
            case 'SKIP':
                toast = {id: id, data: {text: 'Пропускает ход', user: data.user}, options: {}}
                break
            default:
                console.log('Unexpected message:', data)
        }
        setToasts((prevToasts) => [...prevToasts, toast])
        console.log(toasts)

        setTimeout(() => {
            removeToast(id)
        }, 3500)

        return id
    }, [])

    const addSuccess = useCallback((error) => {
        const id = Date.now()
        const toast = {id: id, data: {text: error, user: null}, options: {variant: 'success'}}
        setToasts((prevToasts) => [...prevToasts, toast])
        console.log(toasts)

        setTimeout(() => {
            removeToast(id)
        }, 3500)

        return id
    }, [])

    const addWarning = useCallback((error) => {
        const id = Date.now()
        const toast = {id: id, data: {text: error, user: null}, options: {variant: 'warning'}}
        setToasts((prevToasts) => [...prevToasts, toast])
        console.log(toasts)

        setTimeout(() => {
            removeToast(id)
        }, 3500)

        return id
    }, [])

    const addError = useCallback((error) => {
        const id = Date.now()
        const toast = {id: id, data: {text: error, user: null}, options: {variant: 'error'}}
        setToasts((prevToasts) => [...prevToasts, toast])
        console.log(toasts)

        setTimeout(() => {
            removeToast(id)
        }, 10000)

        return id
    }, [])

    const removeToast = useCallback((id) => {
        setToasts((prevToasts) => prevToasts.filter((toast) => toast.id !== id))
    }, [])

    const value = {addToast, addSuccess, addError, addWarning, removeToast}

    return (<ToastContext.Provider value={value}>
        {children}
        <ToastContainer position="top-end" className="p-3 mt-5">
            {toasts.map((toast) => (<Toast
                key={toast.id}
                onClose={() => removeToast(toast.id)}
                bg={toast.options.variant || 'light'}
            >
                {toast.data.user && <Toast.Header closeButton={false}>
                    <b><Player player={toast.data.user} hideRating={true}/></b>
                </Toast.Header>}
                <Toast.Body>{toast.data.text}</Toast.Body>
            </Toast>))}
        </ToastContainer>
    </ToastContext.Provider>)
}