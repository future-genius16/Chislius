import React, {useState} from 'react'
import {Alert, Button, Modal} from 'react-bootstrap'
import api from '../../client/ApiClient'
import {useAuth} from '../../context/TokenContext'
import {useToast} from '../utils/useToast'

const DeleteUserModal = ({show, onHide}) => {
    const {token, logout} = useAuth()
    const {addSuccess} = useToast()

    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState('')

    const handleClick = () => {
        setIsLoading(false)
        setError('')

        console.log("Leave")
        api.deleteUser(token).then(() => {
            console.log("Comp")
            logout()
            addSuccess("Аккаунт успешно удален")
            onHide()
        }).catch((err) => {
            console.log("Err")
            setError(err.message)
            setIsLoading(false)
        })
    }

    return (<Modal show={show} onHide={onHide}>
        <Modal.Header closeButton>
            <Modal.Title>Удаление аккаунта</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            {error && <Alert variant="danger">{error}</Alert>}
            Вы уверены, что хотите удалить аккаунт?<br/>
            <b>Это действие невозможно отменить!</b>
        </Modal.Body>

        <Modal.Footer>
            <Button variant="outline-primary" onClick={onHide}>Отмена</Button>
            <Button variant="primary" type="submit" onClick={handleClick} disabled={isLoading}>
                {isLoading ? 'Загрузка...' : 'Удалить'}</Button>
        </Modal.Footer>
    </Modal>)
}

export default DeleteUserModal