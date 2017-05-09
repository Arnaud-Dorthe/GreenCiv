/**
 *  Copyright (C) 2002-2017   The FreeCol Team
 *
 *  This file is part of FreeCol.
 *
 *  FreeCol is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  FreeCol is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with FreeCol.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.freecol.common.networking;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.common.io.FreeColXMLReader;
import net.sf.freecol.common.io.FreeColXMLWriter;
import net.sf.freecol.common.model.Game;
import net.sf.freecol.common.model.HighScore;
import net.sf.freecol.server.FreeColServer;
import net.sf.freecol.server.model.ServerPlayer;

import net.sf.freecol.common.util.DOMUtils;
import org.w3c.dom.Element;


/**
 * The message sent when an highScore occurs.
 */
public class HighScoreMessage extends ObjectMessage {

    public static final String TAG = "highScore";
    private static final String KEY_TAG = "key";


    /**
     * Create a new {@code HighScoreMessage} in request form (no
     * scores attached).
     *
     * @param key A message key for the final display.
     * @param scores The list of high scores, or null.
     */
    public HighScoreMessage(String key, List<HighScore> scores) {
        super(TAG, KEY_TAG, key);

        if (scores != null) addAll(scores);
    }

    /**
     * Create a new {@code HighScoreMessage} from a supplied element.
     *
     * @param game The {@code Game} this message belongs to.
     * @param element The {@code Element} to use to create the message.
     */
    public HighScoreMessage(Game game, Element element) {
        this(getStringAttribute(element, KEY_TAG),
             DOMUtils.getChildren(game, element, HighScore.class));
    }

    /**
     * Create a new {@code HighScoreMessage} from a stream.
     *
     * @param game The {@code Game} this message belongs to.
     * @param xr The {@code FreeColXMLReader} to read from.
     * @exception XMLStreamException if there is a problem reading the stream.
     */
    public HighScoreMessage(Game game, FreeColXMLReader xr)
        throws XMLStreamException {
        super(TAG, xr, KEY_TAG);

        List<HighScore> scores = new ArrayList<>();
        while (xr.moreTags()) {
            String tag = xr.getLocalName();
            if (HighScore.TAG.equals(tag)) {
                HighScore hs = xr.readFreeColObject(game, HighScore.class);
                if (hs != null) scores.add(hs);
            } else {
                expected(HighScore.TAG, tag);
            }
            xr.expectTag(tag);
        }
        xr.expectTag(TAG);
        addAll(scores);
    }


    /**
     * Accessor for the key.
     *
     * @return The key.
     */
    private String getKey() {
        return getStringAttribute(KEY_TAG);
    }

    /**
     * Accessor for the scores list.
     *
     * @return The list of {@code HighScore}s.
     */
    private List<HighScore> getScores() {
        return getChildren(HighScore.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public MessagePriority getPriority() {
        return Message.MessagePriority.NORMAL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clientHandler(FreeColClient freeColClient) {
        final String key = getKey();
        final List<HighScore> scores = getScores();

        igc(freeColClient).highScoreHandler(key, scores);
    }
        
    /**
     * {@inheritDoc}
     */
    @Override
    public ChangeSet serverHandler(FreeColServer freeColServer,
                                   ServerPlayer serverPlayer) {
        return igc(freeColServer)
            .getHighScores(serverPlayer, getKey());
    }
}
