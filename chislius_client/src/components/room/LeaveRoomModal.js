import React, {useState} from 'react'
import {Alert, Button, Modal} from 'react-bootstrap'
import api from '../../client/ApiClient'
import {useAuth} from '../../context/TokenContext'

const LeaveRoomModal = ({show, onHide}) => {
    const {token, logout} = useAuth()

    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState('')

    const handleClick = () => {
        setIsLoading(true)
        setError('')

        api.leaveRoom(token).then(() => {
            onHide()
        }).catch((err) => {
            setError(err.message)
            setIsLoading(false)
        })
    }

    return (<Modal show={show} onHide={onHide}>
        <Modal.Header closeButton>
            <Modal.Title>Выход</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            {error && <Alert variant="danger">{error}</Alert>}
            Вы уверены, что хотите выйти?
        </Modal.Body>

        <Modal.Footer>
            <Button variant="secondary" onClick={onHide}>Отмена</Button>
            <Button variant="primary" type="submit" onClick={handleClick} disabled={isLoading}>
                {isLoading ? 'Загрузка...' : 'Выход'}</Button>
        </Modal.Footer>
    </Modal>)
}

export default LeaveRoomModal