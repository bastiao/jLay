package models;
/*
 Copyright (C) 2014 - Luís A. Bastião Silva

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


/**
 * created by bastiao on 03/11/14.
 */

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;


@Entity
public class ConnectionStore extends Model
{

    @Id
    @Constraints.Min(10)
    public Long id;


    public String name;


    public static Finder<Long, ConnectionStore> find = new Finder<Long, ConnectionStore>(
            Long.class, ConnectionStore.class
    );

    public String destination;
    @Constraints.Required
    public String origin;
    public String messageType;

    public ConnectionStore(String destination, String origin, String messageType, Serializable content)
    {

        this.destination = destination ;
        this.origin = origin;
        this.messageType = messageType;
    }

    public void store()
    {
        // pass

    }

    public static ConnectionStore get ()
    {
        return null ;
    }


}