/*
 * Copyright © 2022 - present Thakshila Nuwangi. All rights reserved.
 *  Licensed under the MIT License. See LICENSE.txt in the project root for license information.
 */

package lk.ijse.dep8.orm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Embed {
}
