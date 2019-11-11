/*
   Copyright 2017 Gordon D. Thompson

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package net.ucanaccess.hibernate.dialect;

import java.sql.Types;

import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.unique.UniqueDelegate;
import org.hibernate.type.StandardBasicTypes;

/**
 * 
 * Hibernate dialect for UCanAccess
 * 
 */
public class UCanAccessDialect extends SQLServerDialect {
    
    public UCanAccessDialect() {
        super();
        
        // lets UCanAccess determine if it is working with Hibernate
        System.setProperty(this.getClass().getName() + ".isActive", "true");
        
        registerColumnType(Types.INTEGER, "LONG");
        registerColumnType(Types.BOOLEAN, "YESNO");
        registerColumnType(Types.CLOB, "MEMO");
        registerColumnType(Types.BLOB, "OLE");

        registerFunction("current_date", new StandardSQLFunction("Date", StandardBasicTypes.DATE));
        registerFunction("current_time", new StandardSQLFunction("Time", StandardBasicTypes.TIME));
        registerFunction("current_timestamp", new StandardSQLFunction("Now", StandardBasicTypes.TIMESTAMP));
        registerFunction("second", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "Second(?1)"));
        registerFunction("minute", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "Minute(?1)"));
        registerFunction("hour", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "Hour(?1)"));
    }

    // -----------------------
    // IDENTITY column support
    // -----------------------
    //
    // This is apparently how it was done in older versions of Hibernate:
    //
    // public boolean supportsIdentityColumns() {
    // return true;
    // }
    //
    // public boolean hasDataTypeInIdentityColumn() {
    // return false;
    // }
    //
    // public String getIdentityColumnString() {
    // return "COUNTER";
    // }

    private static final UCanAccessDialectIdentityColumnSupport IDENTITY_COLUMN_SUPPORT =
            new UCanAccessDialectIdentityColumnSupport();

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return (IdentityColumnSupport) IDENTITY_COLUMN_SUPPORT;
    }

    @Override
    public boolean supportsSequences() {
        // TODO Hibernate bug? It does call this method, but then it tries to
        // use Sequences anyway.
        // System.out.println("-> Hibernate is checking support for Sequences.");
        return false;
    }
    
    private static final UCanAccessDialectUniqueDelegate UNIQUE_DELEGATE =
            new UCanAccessDialectUniqueDelegate();

    @Override
    public UniqueDelegate getUniqueDelegate() {
        return (UniqueDelegate) UNIQUE_DELEGATE;
    }
    
    private static final UCanAccessDialectLimitHandler LIMIT_HANDLER =
            new UCanAccessDialectLimitHandler();

    @Override
    public LimitHandler getLimitHandler() {
        return LIMIT_HANDLER;
    }
     
}
