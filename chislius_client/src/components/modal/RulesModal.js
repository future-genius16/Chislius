import React from 'react'
import {Button, Container, Image, Modal} from 'react-bootstrap'

const RulesModal = ({show, onHide}) => {
    return (<Modal
        show={show}
        onHide={onHide}
        size={'xl'}
        dialogClassName="modal-dialog-scrollable"
    >
        <Modal.Header closeButton>
            <Modal.Title id="example-custom-modal-styling-title">
                Правила игры
            </Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <Container>
                <Image src={'images/rules.svg'} fluid/>
            </Container>
        </Modal.Body>
        <Modal.Footer>
            <Button variant="outline-primary" onClick={onHide}>
                Закрыть
            </Button>
        </Modal.Footer>
    </Modal>)
}

export default RulesModal