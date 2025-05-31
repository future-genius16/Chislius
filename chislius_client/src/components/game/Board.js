import Card from './Card'
import {Container} from 'react-bootstrap'

function Board({onClick, cards}) {
    return (<Container className="board__cards-container p-0">
        {cards.map((card) => (<Card key={card.id} card={card} onClick={onClick}/>))}
    </Container>)
}

export default Board