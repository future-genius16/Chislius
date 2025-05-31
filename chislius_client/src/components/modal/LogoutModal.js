import React from 'react'
import {Button, Modal} from 'react-bootstrap'
import {useAuth} from '../../context/TokenContext'

const LogoutModal = ({show, onHide}) => {
    const {logout} = useAuth()
    const handleClick = async () => {
        logout()
        onHide()
    }

    return (<Modal show={show} onHide={onHide}>
        <Modal.Header closeButton>
            <Modal.Title>Выход</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            Вы уверены, что хотите выйти?
        </Modal.Body>

        <Modal.Footer>
            <Button variant="outline-primary" onClick={onHide}>Отмена</Button>
            <Button variant="primary" type="submit" onClick={handleClick}>Выход</Button>
        </Modal.Footer>
    </Modal>)
}

export default LogoutModal