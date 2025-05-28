import React, {useState} from 'react'
import {Alert, Button, Modal} from 'react-bootstrap'
import api from '../../client/ApiClient'

const LeaveRoomModal = ({show, onHide}) => {
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState('')

    const handleClick = async () => {
        setIsLoading(true)
        setError('')

        try {
            await api.leaveRoom()
            onHide()
        } catch (err) {
            setError(err.message)
        } finally {
            setIsLoading(false)
        }
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